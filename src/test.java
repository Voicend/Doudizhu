import Poker.*;
import Verification.Verify;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class test {
    public static void main(String[] args) {
        ArrayList<Poker> list=new ArrayList<>();
        for(int i=1;i<=13;i++){
            list.add(new Poker(i,Poker.spade));
        }
        Gson gson=new Gson();
        String test=gson.toJson(list);

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("id",2);
        jsonObject.addProperty("list",test);
        System.out.println(test);
        Verify verify=Verify.isCardsValid(PokerConversion.Poker2Int(list),null);
        test=gson.toJson(verify);
        System.out.println(test);
        Verify verify1=gson.fromJson(test,Verify.class);
        ArrayList<Poker> list1=verify1.getPokerList();
        for(int i=0;i<list1.size();i++){
            System.out.println(list1.get(i).toString());
        }
//        System.out.println(jsonObject.toString());
//        ArrayList<Poker> finallist=gson.fromJson(jsonObject.get("list").getAsString(),new  TypeToken<ArrayList<Poker>>(){}.getType());
//        JsonArray array=gson.fromJson(jsonObject.get("list").getAsString(),JsonArray.class);
//        for(int i=0;i<finallist.size();i++){
//            System.out.println(finallist.get(i).toString());
//        }
    }
}
