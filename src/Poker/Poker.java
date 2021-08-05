package Poker;

import java.io.Serializable;

//这是纸牌类。
//id表示数字，j=11,q=12,k=13,A=1
//color表示花色，club是梅花，diamond是方块，spade是黑桃，heart是红桃
//bigJoker表示大王，littleJoker表示小王
//两个构造函数，第一个是数字，第二个是花色，或者直接调用Poker(Poker.bigJoker)创建大王
//一个函数large比较当前对象和参数对象大小。
public class Poker implements Comparable<Poker>,Serializable{
    public int id,color;
    public final static int bigJoker=1,littleJoker=0;
    public final static int club=0,spade=1,diamond=2,heart=3,joker=5;
    public final static String[] category={"club","spade","diamond","heart","","joker"};
    public Poker(int id,int color){this.id=id;this.color=color;}
    public Poker(int id){this.id=id;this.color=joker;}
    public boolean larger(Poker tmp){
            if(this.id==0)return false;
            else
            return actValue(this)>actValue(tmp);
    }

    @Override
    public int compareTo(Poker o) {
        if(this.larger(o))
            return 1;
        else if(o.larger(this))
            return -1;
        else
            return 0;
    }
    public boolean largeOne(Poker tmp){//tmp比this大1
        return actValue(this)+1==actValue(tmp);
    }

    public boolean isBigJoker(){
        if(color==joker&&id==bigJoker)
            return true;
        else
            return false;
    }
    public boolean isLittleJoker(){
        if(color==joker&&id==littleJoker)
            return true;
        else
            return false;
    }
    private int actValue(Poker i ){
        if(i.color==joker){
            return i.id+20;
        }
        else if(i.id==1||i.id==2)
            return i.id+13;
        else
            return i.id;
    }

    @Override
    public String toString() {
        return "id:"+id+" color:"+category[color];
    }
}
