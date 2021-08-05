package Handler;

import Poker.*;
import UIIG.UIwithHandler;
import Verification.Verify;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

import static clientNet.Connect.connect;

public class Handler {
    private JsonParser jsonParser=new JsonParser();
    private Gson gson=new Gson();
    private int mySeat,latterSeat,formerSeat,lordSeat;//我的座位号，上下家的座位号，地主座位号
    private int[] leftCardsNum={17,17,17};//剩余手牌数量计算，当为0则游戏结束，
    private ArrayList<Poker> myPokerList,lord3Cards;//我的手牌，地主3张牌
    private UIwithHandler ui;//UI
    private Verify prev,myVerify;//前一次判定规则和牌
    private int currentTurnSeat,statisticTurns=0;//currentTurnSeat，当前是谁的回合；statisticTurn统计连续未出牌数，连续两人未出牌则规则清零
    //构造函数
    public Handler(UIwithHandler ui){
        this.ui=ui;
        ui.setMyhandler(this);
        prev=null;
        myVerify=null;
    }
    public  void handleReciever(String content){
        JsonObject details= jsonParser.parse(content).getAsJsonObject();
        int id=details.get("id").getAsInt();
        switch (id){
            case 0:
                mySeat=details.get("seat").getAsInt();
                latterSeat=(mySeat+1)%3;
                formerSeat=(mySeat+2)%3;
                myPokerList=gson.fromJson(details.get("handpokers").getAsString(),new TypeToken<ArrayList<Poker>>(){}.getType());
                lord3Cards=gson.fromJson(details.get("pokers").getAsString(),new TypeToken<ArrayList<Poker>>(){}.getType());
                ui.mycard=(PokerConversion.Poker2Int(myPokerList));
                ui.lordcard=PokerConversion.Poker2Int(lord3Cards);
                int CallLordSeat=details.get("p").getAsInt();
                ui.startgame();
                ui.startcalllord(convertSeat2local(CallLordSeat),0);

                //TODO:something left to do,1.show lord 3 cards
                break;
            case 1:
                int currentCallLordSeat;
                currentCallLordSeat=details.get("seat").getAsInt();
                if(currentCallLordSeat!=mySeat)
                    ui.calllordscore(convertSeat2local((currentCallLordSeat-1)%3),details.get("last").getAsInt());
                //TODO:calllord
                ui.startcalllord(convertSeat2local(currentCallLordSeat),details.get("score").getAsInt());
                break;
            case 2:
                lordSeat=details.get("seat").getAsInt();
                leftCardsNum[convertSeat2local(lordSeat)]+=3;
                currentTurnSeat=lordSeat;
                if(lordSeat==mySeat){
                    myPokerList.addAll(lord3Cards);
                    Collections.sort(myPokerList);
                    ui.setMycard(PokerConversion.Poker2Int(myPokerList));
                    ui.startturn(0);//开始地主（我）的回合
                }
                ui.setlord(convertSeat2local(lordSeat));
                break;
            case 3:
                currentTurnSeat=details.get("seat").getAsInt();
                boolean isOut=false;//是否出牌
                isOut=details.get("out").getAsBoolean();
                if(isOut){//出牌
                    prev=gson.fromJson( details.get("verify").getAsString(),Verify.class);
                    ui.setChupaiqu(convertSeat2local(currentTurnSeat),prev.getCardArray(),leftCardsNum[convertSeat2local(currentTurnSeat)]-prev.length());
                    leftCardsNum[convertSeat2local(currentTurnSeat)]=leftCardsNum[convertSeat2local(currentTurnSeat)]-prev.length();
                    if(leftCardsNum[convertSeat2local(currentTurnSeat)]==0){
                        if(currentTurnSeat==lordSeat){
                            //TODO:show the win of lord
                        }else{
                            //TODO:show the win of farmer
                        }
                    }
                }
                else{//未出牌
                    statisticTurns++;
                    if(statisticTurns==2){
                        prev=null;
                    }
                }
                if(currentTurnSeat==formerSeat)
                    ui.startturn(0);
                else if(currentTurnSeat==latterSeat)
                    ui.startturn(1);
                break;
        }
    }
    public void handleAction(int index){//index=3则表示出牌,index=1表示不叫地主,index=2表示选地主叫分,index=0表示准备完毕，index=4则表示不出,
        String sender=null;
        JsonObject callPoint=new JsonObject();
        switch (index){
            case 3:
                int[] cardList =ui.getchosencards();
                Verify temp;
                temp=Verify.isCardsValid(cardList,prev);
                if(temp.isValid()){
                    myVerify=temp;
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("id",3);
                    jsonObject.addProperty("seat",mySeat);
                    jsonObject.addProperty("out",true);
                    jsonObject.addProperty("verify",gson.toJson(myVerify));
                    sender=jsonObject.toString();
                    leftCardsNum[0]-=myVerify.length();
                    ui.Chupai();
                    connect(sender);
                    if(myVerify.hasKingBomb()){
                        ui.startRocket();
                    }
                }
                if(leftCardsNum[0]==0){
                    JsonObject jsonObject=new JsonObject();
                    jsonObject.addProperty("id",5);
                    jsonObject.addProperty("seat",mySeat);
                    connect(sender);
                }
                break;
            case 4:
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("id",3);
                jsonObject.addProperty("seat",mySeat);
                jsonObject.addProperty("out",false);
                sender=jsonObject.toString();
                connect(sender);
                break;
            case 0:
                JsonObject ready=new JsonObject();
                ready.addProperty("id",0);
                sender=ready.toString();
                connect(sender);
                break;
            case 5:
                callPoint.addProperty("id",1);
                callPoint.addProperty("seat",mySeat);
                callPoint.addProperty("score",0);
                connect(sender);
                break;
            case 6:
                callPoint.addProperty("id",1);
                callPoint.addProperty("seat",mySeat);
                callPoint.addProperty("score",1);
                connect(sender);
                break;
            case 7:
                callPoint.addProperty("id",1);
                callPoint.addProperty("seat",mySeat);
                callPoint.addProperty("score",2);
                connect(sender);
                break;
            case 8:
                callPoint.addProperty("id",1);
                callPoint.addProperty("seat",mySeat);
                callPoint.addProperty("score",3);
                connect(sender);
                break;


        }

    }
    private int convertSeat2local(int seat){//根据座位号转换为本地客户端的相对位置
        if(seat==mySeat)
            return 0;
        else if(seat==latterSeat){
            return 2;

        }else if(seat==formerSeat){
            return 1;
        }
        return 0;
    }//根据座位号得到UI位置
    private int getSeatNum(int position){
        switch (position){
            case 0:
                return mySeat;
            case 1:
                return formerSeat;
            case 2:
                return latterSeat;
        }
        return 0;
    }//根据UI位置获得实际座位号

}
