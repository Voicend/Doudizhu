package Verification;

import Poker.Poker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static Poker.PokerConversion.*;
//单张————————rule1
//大王（彩色Joker）>小王（黑白Joker）>2>A>K>Q>J>10>9>8...>3，不计花色，上家出3，下家必须出4或以上
//一对————————rule2
//22>AA>...>33，不计花色，上家出33，必须出44或以上。一对王并称为火箭，下详解。

//上家出三带一，必须跟三带一；上家出三不带，必须出三不带
//单顺————————rule1
//最少5张牌，最多12张牌（3⋯A），不能有2，不计花色
//例如：345678，78910JQ，345678910JQKA
//上家出6只，必须跟6只；上家出10只，必须跟10只，如此类推
//双顺————————rule2
//又称连对，最少连3对，最多连10对（33⋯QQ），不能带2，不计花色
//例如：778899，445566，334455667788991010JJQQ
//上家出6对，必须跟6对；上家出3对，必须跟3对，如此类推

//飞机————————rule3
//飞机分三种，飞机不带翼，飞机带小翼，飞机带大翼
//飞机是两个连续数字的三条，最少2连
//例如：333444，777888999101010JJJQQQ
//飞机不带翼，即纯粹飞机，例如：444555
//飞机带小翼，即连续多于一个三带一
//飞机带大翼，即连续多于一个三带二
//例如：33344456，77788834，101010JJJQQQ335577，6667778883399JJ
//如上家出飞机不带翼必须跟飞机不带翼，如上家出飞机带翼必须跟飞机带翼
//任何情况下，其三条部分都不能有2

//炸弹————————rule5
//即四条，如：9999，QQQQ，每次打出时，赌注乘二
//炸弹大于除火箭外的一切牌型。点数大的炸弹大于点数小的炸弹，如:4444>3333，最大的炸弹是2222
//另有“小三炸弹”，需是三只2加大王或小王，但这是最小的炸弹，即是 3333>小三炸弹。（很多地方没有这种牌型）
//火箭——————————————rule6
//即大王+小王，打出时，赌注乘二
//火箭大于所有牌型
public class Verify implements Serializable{
    private Verify PrevV;
    private ArrayList<Poker> list;//牌
    private ArrayList<Poker>[] set=new ArrayList[4] ;//牌数为1，2，3，4的集合
    private int num[]=new int[4];//牌数为1，2，3，4的个数
    private int cnt[]=new int[4];//游标
    private boolean hasBJ=false,hasLJ=false;//是否存在大小王
    private boolean valid=false;
    private int rule=-1;//规则
    private int addInfo=-1,addInfo2=-1;//附加信息

    //获取信息
    public int[] getCardArray() {
        return Poker2Int(list);
    }//获取int数组型牌组
    public ArrayList<Poker> getPokerList() {
        return list;
    }//获取List型牌组
    public boolean isValid() {
        return valid;
    }//当前牌是否符合规则
    public int length(){return list.size();}//当前牌总数
    public int getRule() {
        return rule;
    }//获取当前牌的规则,-1表示尚未含有规则
    public int getAddInfo(){return addInfo;}//获取当前附加信息
    public int getAddInfo2(){return addInfo2;}
    //构造函数
    protected Verify(ArrayList<Poker> list, Verify PrevV){
        this.PrevV=PrevV;
        this.list=list;
        for(int i=0;i<4;i++) {
            num[i] = 0;
            cnt[i]=0;
        }
    }
    //判断规则方法
    public static Verify isCardsValid(int [] cards){return isCardsValid(cards,null);}
    public static Verify isCardsValid(int [] cards,Verify Prev){
        Verify verify=statistic(Int2Poker(cards),Prev);//先对出牌进行统计
        if(verify.hasKingBomb()) {//不论什么牌，先判断是否为王炸，如果是，则直接结束判断
            verify.valid = true;
            verify.rule=6;
            return verify;
        }else if(Prev!=null&& Prev.hasKingBomb()){
            verify.valid=false;
            return verify;
        }
        if(verify.isBomb()){
            if (Prev!=null&& Prev.rule==5&&!verify.getMaxFour().larger(Prev.getMaxFour())){
                verify.valid=false;
            }else{
                verify.valid=true;
                verify.rule=5;
            }
            return verify;
        }
        if(Prev!=null){
            switch (Prev.getRule()){
                case 4:
                    verify.fourCardsJudge(Prev,Prev.addInfo);
                    return verify;
                case 3:
                    verify.threeCardsJudge(Prev,Prev.addInfo);
                    return verify;
                case 2:
                    verify.twoCardsJudeg(Prev,Prev.addInfo);
                    return verify;
                case 1:
                    verify.singleCardsJudge(Prev,Prev.addInfo);
                    return verify;

            }
        }else {
            verify.judgeAllRule();
        }
        return verify;
    }
    private void judgeAllRule(){
        if(hasFour()){//如果有4张牌的，就先判断是否为4带2，4带两对，或者航天飞机
            if(num[3]==1&&length()==6){
                sign(true,4,1);
                return;
            }
            if(num[3]==1&&length()==8&&num[1]==2){
                sign(true,4,2);
                return;
            }
            if (num[3] >1&& length()==num[3]*4 && isSuccession(3,0) ) {//飞机不带翼
                sign(true,4,3,1);
                return ;
            }
            if (num[3] >1 && length()==num[3]*(4+1) &&num[3]==num[0]&& isSuccession(3,0) ) {//飞机带1
                sign(true,4,3,2);
                return ;
            }
            if (num[3] >1&& length()==num[3]*(4+2) && num[3]==num[1]&& isSuccession(3,0)) {//飞机带对
                sign(true,4,3,3);
                return ;
            }
        }
        if(hasThree()){//没有4张牌的或者不符合四张牌规则的，如果含有3张牌则在这里做处理
            change423();
            if(num[2]>=1&&isSuccession(2,0)&&length()==num[2]*3){//3-1,不带翼
                if(num[2]==1||getMaxThree().id!=2) {
                    sign(true,3,1);
                    return ;
                }
            }
            if(num[2]>=1&&isSuccession(2,0)&&length()==num[2]*(3+1)){
                if(num[2]==1||getMaxThree().id!=2) {
                    sign(true,3,2);
                    return ;
                }
            }
            if(num[2]>=1&&num[1]==num[2]&&isSuccession(2,0)&&length()==num[2]*(3+2)){
                if(num[2]==1||getMaxThree().id!=2) {
                    sign(true,3,3);
                    return ;
                }
            }
        }
        if(hasTwo()){
            if(num[1]>=1&&isSuccession(1,0)&&length()==2*num[1]){
                if(num[1]==1||getMaxTwo().id!=2) {
                    sign(true,2);
                    return ;
                }
            }
        }
        if(hasOne()){
            if(num[0]>=1&&isSuccession(0,0)&&length()==num[0]){
                if(num[0]==1||getMaxOne().id!=2) {
                    sign(true,1);
                    return ;
                }
            }
        }
        sign(false);
        return;
    }
    private void fourCardsJudge(Verify Prev,int mode){
        switch (mode){
            case 1:
                //四带二————————rule4
                //四条加2张任意牌，或四条加两对————rule4-1
                //四条加2只（两只可相同），即888857，JJJJ77，2222QQAA————rule4-2
                //如上家出四条加两只必须跟四条加两只
                //四带二效果不等同炸弹，只当作普通牌型使用
                if(num[3]==1&&length()==6&&getMaxFour().larger(Prev.getMaxFour())){
                    sign(true,4,1);
                    return ;
                }else{
                    sign(false);
                    return ;
                }
            case 2:
                if(num[3]==1&&num[1]==2&&length()==8&&getMaxFour().larger(Prev.getMaxFour())){
                    sign(true,4,2);
                    return ;
                }else{
                    sign(false);
                    return ;
                }
            case 3:
                //航天飞机————————rule4—3
                //此种牌型极少出现，但仍有理论之可能。4——3——1
                //亦即连续数字的四条，同样可以不带翼，带翼4——3——2，4——3——3
                //如：
                //不带翼: 33334444
                //带翼: 44445555 37 JQ
                switch (addInfo) {
                    case 1:
                        if (num[3] == Prev.num[3]&& length()==num[3]*4 && isSuccession(3,0) && getMaxFour().larger(Prev.getMaxFour())) {
                            sign(true,4,3,1);
                            return;
                        } else {
                            sign(false);
                            return ;
                        }
                    case 2:
                        if (num[3] == Prev.num[3]&& length()==num[3]*(4+1) && isSuccession(3,0) && getMaxFour().larger(Prev.getMaxFour())) {
                            sign(true,4,3,2);
                            return ;
                        } else {
                            sign(false);
                            return ;
                        }
                    case 3:
                        if (num[3] == Prev.num[3]&& length()==num[3]*(4+2) && isSuccession(3,0) && getMaxFour().larger(Prev.getMaxFour())) {
                            sign(true,4,3,3);
                            return ;
                        } else {
                            sign(false);
                            return ;
                        }
                }
        }
    }//mode即为Prev.addinfo，下同
    private void threeCardsJudge(Verify Prev,int mode){
        switch (mode){
            case 1:
                //三带————————rule3
                //三带有分3种，但大前提都是2>A>...>3，并只以三条的部分比大小。
                //三不带（三带零），即三条，例如222，AAA，666，888等  3-1
                //三带一，即三条+一只，例如2223，AAAJ，6669等   3-2
                //三带二，即三条+一对，例如22233，AAAJJ等   3-3
                if(num[2]==Prev.num[2]&&isSuccession(2,0)&&length()==Prev.length()&&getMaxThree().larger(Prev.getMaxThree())){
                    if(num[2]==1||getMaxThree().id!=2) {
                        sign(true,3,1);
                        return ;
                    }
                }
                    valid=false;
                    return ;
            case 2:
                if(num[2]==Prev.num[2]&&isSuccession(2,0)&&length()==Prev.length()&&getMaxThree().larger(Prev.getMaxThree())){
                    if(num[2]==1||getMaxThree().id!=2) {
                        sign(true,3,2);
                        return ;
                    }
                }
                    valid=false;
                    return ;
            case 3:
                if(num[2]==Prev.num[2]&&num[1]==Prev.num[1]&&isSuccession(2,0)&&length()==Prev.length()&&getMaxThree().larger(Prev.getMaxThree())){
                    if(num[2]==1||getMaxThree().id!=2) {
                        sign(true,3,3);
                        return ;
                    }
                }
                    sign(false);
                    return ;
        }
    }
    private void twoCardsJudeg(Verify Prev,int mode){
        if(num[1]==Prev.num[1]&&isSuccession(1,0)&&length()==Prev.length()&&getMaxTwo().larger(Prev.getMaxTwo())){
            if(num[1]==1||getMaxTwo().id!=2) {
                sign(true,2);
                return ;
            }
        }
        sign(false);
    }
    private void singleCardsJudge(Verify Prev,int mode){
        if(num[0]==Prev.num[0]&&isSuccession(0,0)&&length()==Prev.length()&&getMaxOne().larger(Prev.getMaxOne())){
            if(num[0]==1||getMaxOne().id!=2) {
                sign(true,1);
                return ;
            }
        }
        sign(false);
    }
    private boolean isSuccession(int listNum,int successNum){//判断第listnum列是否连续successnum个，如果successnum为0则为list全长
        int length=successNum!=0?successNum:set[listNum].size();
        if(length==1)
            return true;
        for(int i=0;i<length-1;i++){
            Poker former=set[listNum].get(i),latter=set[listNum].get(i+1);
            if(!latter.largeOne(former))
                return false;
        }
        return true;
    }
    private boolean isBomb(){return hasFour()&&length()==4;}//rule5，炸弹
    public boolean hasKingBomb(){
        return hasBJ&&hasLJ;
    }//rule6，火箭
    //统计方法
    protected static Verify statistic(ArrayList<Poker> list,Verify PrevV){
        Verify verify=new Verify(list,PrevV);
        Poker temp=list.get(0);
        int count=1;
        for(int i=1;i<list.size();i++){//统计牌数为1，2，3，4的个数
            Poker item=list.get(i);
            if(item.compareTo(temp)==0){
                count++;
            }
            else {
                verify.num[count-1]++;
                count=1;
                temp=item;
            }
        }
        for(int i=0;i<4;i++)//根据num初始化set数组
            verify.set[i]=new ArrayList<>();
        temp=list.get(0);
        count=1;
        for(int i=1;i<list.size();i++){//牌数为1，2，3，4的入队列
            Poker item=list.get(i);
            if(item.compareTo(temp)==0){
                count++;
            }
            else {
                verify.set[count-1].add(temp);//牌数为count的temp根据游标cnt[count]入队列，cnt[count]加一
                verify.cnt[count-1]=(verify.cnt[count-1]+1)%verify.num[count-1];
                count=1;
                temp=item;
            }
            if(item.isBigJoker())//是否存在大小王
                verify.hasBJ=true;
            if(item.isLittleJoker())
                verify.hasLJ=true;
        }
        return verify;
    }
    protected boolean hasThree(){
        return num[2]!=0;
    }//是否存在牌数为4的牌，下同理
    protected boolean hasFour(){
        return num[3]!=0;
    }
    protected boolean hasTwo(){
        return num[1]!=0;
    }
    protected boolean hasOne(){
        return num[0]!=0;
    }
    protected Poker getNext(int i){
        if(cnt[i]>=num[i]){
            cnt[i]=0;
            return null;
        }
        Poker tmp=set[i].get(cnt[i]);
        cnt[i]=cnt[i]+1;
        return tmp;
    }//取下一个牌数为4的牌id，下同
    protected Poker getMaxFour(){
        if(num[3]!=0){
            return set[3].get(num[3]-1);
        }
        return null;
    }//取最大的牌数为4的牌，下同
    protected Poker getMaxThree(){
        if(num[2]!=0){
            return set[2].get(num[2]-1);
        }
        return null;
    }
    protected Poker getMaxTwo(){
        if(num[1]!=0){
            return set[1].get(num[1]-1);
        }
        return null;
    }
    protected Poker getMaxOne(){
        if(num[0]!=0){
            return set[0].get(num[0]-1);
        }
        return null;
    }
    //辅助方法
    protected void change423(){//change 4 to 3，避免3+1归为4
        if(hasFour()){
            for(Poker item=getNext(3);item!=null;item=getNext(3)) {//把每个牌数为4的拆成1+3
                num[0]++;//1多一个
                num[2]++;//3多一个
                set[0].add(item);//入list
                set[2].add(item);
            }
            Collections.sort(set[0]);//排序
            Collections.sort(set[2]);
            num[3]=0;//
        }
    }
    protected void sign(boolean valid,int rule,int addInfo,int addInfo2){
        this.valid=valid;
        this.rule=rule;
        this.addInfo=addInfo;
        this.addInfo2=addInfo2;
    }
    protected void sign(boolean valid){
        sign(valid,-1,-1,-1);
    }
    protected void sign(boolean valid,int rule){
        sign(valid,rule,-1,-1);
    }
    protected void sign(boolean valid,int rule,int addInfo){
        sign(valid,rule,addInfo,-1);
    }
}
