package Poker;


import java.util.ArrayList;
import java.util.Collections;


//int a 表示卡牌时： 黑桃：0-12；
//                   红桃：13-25；
//                   草花：26-38；
//                   方块：39-51；
//                   小王：52；大王：53,；大于53会返回卡背
public class PokerConversion {

    public static int[] Poker2Int(ArrayList<Poker> list){
        //Poker转int函数，传入一个Poker的Arryalist
        Collections.sort(list);
        if(list.size()==0)
            return null;
        int [] result=new int[list.size()];
        for(int i=0;i<list.size();i++){
            Poker item=list.get(i);
            if(item.color==Poker.joker){
                if(item.id==Poker.bigJoker)
                    result[i]=53;
                else
                    result[i]=52;
                continue;
            }else if(item.color==Poker.spade){
                result[i]=item.id-1;
            }else if(item.color==Poker.heart){
                result[i]=item.id+12;
            }else if(item.color==Poker.diamond){
                result[i]=item.id+38;
            }else if(item.color==Poker.club){
                result[i]=item.id+25;
            }
        }
        return result;
    }
    public static ArrayList<Poker> Int2Poker(int [] array){
        //int转Poker的ArrayList函数，传入int数组
        ArrayList<Poker> list=new ArrayList<>();
        for(int i=0;i<array.length;i++){
            Poker item;
            if(array[i]==53)
                item=new Poker(Poker.bigJoker);
            else if(array[i]==52)
                item=new Poker(Poker.littleJoker);
            else if(array[i]<=12)
                item=new Poker(array[i]+1, Poker.spade);
            else if(array[i]<=25)
                item=new Poker(array[i]-12,Poker.heart);
            else if (array[i]<=38)
                item=new Poker(array[i]-25,Poker.club);
            else
                item=new Poker(array[i]-38,Poker.diamond);
            list.add(item);
        }
        Collections.sort(list);
        return list;
    }
}
