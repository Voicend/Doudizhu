package UIIG;
import Handler.Handler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class maininterface {

    public void setMyhandler(Handler myhandler) {
        this.myhandler = myhandler;
    }
    private JFrame mainframe;
    private static final int mfwidth = 1200;
    private static final int mfheight = 800;
    private JPanel showlordcard;//地主牌展示区(扑克牌大小为105*150)
    private static final int slcwidth = 350;
    private JPanel shoupai[] = new JPanel[3];//手牌区*3
    private static final int spheight = 435;
    private static final int spwidth = 580;
    private Mypanel chupaiqu[] = new Mypanel[3];//出牌区*3
    private static final int cpqwidth = 480;//(480)
    private static final int cpqheight = 320;//(150+150+20)
    private JLabel paiku;//牌库
    private static final int cardwidth = 105;
    private static final int cardheight = 150;
    //JMenuItem start, exit, about;// 定义菜单按钮
    private JButton lordscore[] = new JButton[4];//1分，2分，3分，不叫
    private JButton chupai;//出牌
    private JButton buchu;//不出
    private static final int buttonwidth = 70;
    private static final int buttonheight = 20;
    private JTextField buchusign[] = new JTextField[3];//用于在出牌区显示文字
    private static final int buchuwidth = 80;
    private static final int buchuheight = 35;
    private Clock clock[]=new Clock[3];//计时器旁边的闹钟图案*3
    private static final int clocklength = 50;
    private timeclick time[]=new timeclick[3]; //计时器*3
    private JTextField cardsnumber[]=new JTextField[2];//牌数*2
    private JLabel lordsign;//地主标记
    private Card card[];//玩家手牌
    private JLabel others[][] = new JLabel[2][20];
    private JLabel rocket;
    private Handler myhandler;
    //定义图片
    private ImageIcon back;
    private ImageIcon spade[] = new ImageIcon[13];
    private ImageIcon club[] = new ImageIcon[13];
    private ImageIcon diamond[] = new ImageIcon[13];
    private ImageIcon heart[] = new ImageIcon[13];
    private ImageIcon bigjoker;
    private ImageIcon littlejoker;

    //接口变量
    public int whoislord;//谁是地主：0=玩家，1=左边，2=右边
    public int lordcard[] = {53,52,51};//地主牌
    public int mycard[];//玩家牌
    public int otherscards[] = {17,17};//两位玩家手牌数量，0为左边，1为右边

    public maininterface(){
        //卡牌图片
        back = new ImageIcon("card\\back.png");
        littlejoker = new ImageIcon("card\\littlejoker.jpg");
        bigjoker = new ImageIcon("card\\bigjoker.jpg");
        for(int i=1;i<14;i++){
            spade[i-1] = new ImageIcon("card\\a"+i+".jpg");
            heart[i-1] = new ImageIcon("card\\b"+i+".jpg");
            club[i-1] = new ImageIcon("card\\c"+i+".jpg");
            diamond[i-1] = new ImageIcon("card\\d"+i+".jpg");
        }
        ImageIcon clockimg = new ImageIcon("clock.png");
        ImageIcon lordimg = new ImageIcon("lord.png");
        mainframe = new JFrame("简单的斗地主小游戏");
        mainframe.setPreferredSize(new Dimension(mfwidth,mfheight));
        mainframe.setLayout(null);
        //--------------------------------------------------------------------------------------------------------------
        //动画组件
        rocket = rocket();
        //文字
        //卡牌数量
        for(int i=0;i<2;i++){
            cardsnumber[i] = new JTextField();
            cardsnumber[i].setFont(new Font("宋体",Font.BOLD,18));
            cardsnumber[i].setBorder(new EmptyBorder(0,0,0,0));
            mainframe.add(cardsnumber[i]);
            cardsnumber[i].setEditable(false);
        }
        cardsnumber[0].setBounds(50+cardwidth-100,70,100,30);
        cardsnumber[1].setBounds(mfwidth-65-100,70,100,30);
        //地主叫分

        //不出
        for(int i=0;i<3;i++){
            buchusign[i] = new JTextField();
            buchusign[i].setFont(new Font("黑体",Font.BOLD,24));
            buchusign[i].setHorizontalAlignment(JTextField.CENTER);
            mainframe.add(buchusign[i]);
            buchusign[i].setBorder(new EmptyBorder(0,0,0,0));
            buchusign[i].setVisible(false);
            buchusign[i].setEditable(false);
        }
        buchusign[0].setBounds((mfwidth-300)/2,mfheight-cpqheight,300,buchuheight);
        buchusign[1].setBounds(200,250,buchuwidth,buchuheight);
        buchusign[2].setBounds(mfwidth-200-buchuwidth,250,buchuwidth,buchuheight);
        buchusign[0].setVisible(true);
        buchusign[0].setText("等待其他玩家进入游戏...");
        //--------------------------------------------------------------------------------------------------------------
        //Buttons
        //测试按钮，可删--------------------------------------------------------------------------------------------------
        JButton startgame = new JButton("游戏开始");
        mainframe.add(startgame);
        startgame.setBounds(0,mfheight-190,100,30);
        startgame.addActionListener(e -> startgame());
        JButton start = new JButton("回合开始");
        mainframe.add(start);
        start.setBounds(0,mfheight-100,100,30);
        start.addActionListener(e -> startturn(0));
        JButton calllord = new JButton("开始叫分");
        mainframe.add(calllord);
        calllord.setBounds(0,mfheight-160,100,30);
        calllord.addActionListener(e -> startcalllord(0,0));
        JButton chooselord = new JButton("选出地主");
        mainframe.add(chooselord);
        chooselord.setBounds(0,mfheight-130,100,30);
        chooselord.addActionListener(e -> setlord(0));
        JButton rocketsend = new JButton("火箭发射");
        mainframe.add(rocketsend);
        rocketsend.setBounds(0,mfheight-70,100,30);
        rocketsend.addActionListener(e -> new setrocket().start());
        //--------------------------------------------------------------------------------------------------------------
        //叫地主
        for(int i=1;i<4;i++){
            lordscore[i] = new JButton(i+"分");
            mainframe.add(lordscore[i]);
            lordscore[i].setVisible(false);
            lordscore[i].setBounds(mfwidth/2+35+(buttonwidth+70)*(i-3),
                    mfheight-70-cardheight-buttonheight,buttonwidth,buttonheight);
        }
        lordscore[0] = new JButton("不叫");
        mainframe.add(lordscore[0]);
        lordscore[0].setVisible(false);
        lordscore[0].setBounds(mfwidth/2+buttonwidth+105,
                mfheight-70-cardheight-buttonheight,buttonwidth,buttonheight);

        //出牌
        chupai = new JButton("出牌");
        mainframe.add(chupai);
        chupai.setVisible(false);
        chupai.setBounds(mfwidth/2-buttonwidth-70,
                mfheight-70-cardheight-buttonheight,buttonwidth,buttonheight);
        //不出
        buchu = new JButton("不出");
        mainframe.add(buchu);
        buchu.setVisible(false);
        buchu.setBounds(mfwidth/2+70,
                mfheight-70-cardheight-buttonheight,buttonwidth,buttonheight);
        //地主标记
        lordsign = new JLabel(lordimg);
        mainframe.add(lordsign);
        //--------------------------------------------------------------------------------------------------------------
        //游戏区域
        //出牌区
        for(int i=0;i<3;i++){
            chupaiqu[i] = new Mypanel();
            chupaiqu[i].setOpaque(false);
            chupaiqu[i].setLayout(null);
            mainframe.add(chupaiqu[i]);
        }
        chupaiqu[0].setBounds((mfwidth-spwidth)/2,mfheight-50-cpqheight,spwidth,cpqheight);
        chupaiqu[1].setBounds(50,200,cpqwidth,cardheight+50);
        chupaiqu[2].setBounds(mfwidth-65-cpqwidth,200,cpqwidth,cardheight+50);
        //地主牌展示区
        showlordcard = new JPanel();
        showlordcard.setLayout(null);
        mainframe.add(showlordcard);
        showlordcard.setBounds((mfwidth-slcwidth)/2,0,slcwidth,cardheight);
        //手牌区
        for(int i=0;i<3;i++){
            shoupai[i] = new JPanel();
            shoupai[i].setLayout(null);
            mainframe.add(shoupai[i]);
        }
        shoupai[0].setBounds((mfwidth-spwidth)/2,mfheight-70-cardheight,spwidth,cardheight+20);
        shoupai[1].setBounds(50,100,cardwidth,spheight);
        shoupai[2].setBounds(mfwidth-65-cardwidth,100,cardwidth,spheight);
        //牌库
        paiku = new JLabel(back);
        mainframe.add(paiku);
        paiku.setBounds((mfwidth-cardwidth)/2,250,cardwidth,cardheight);
        //时钟
        for(int i=0;i<3;i++){
            clock[i] = new Clock(clockimg);
            clock[i].a = i;
            mainframe.add(clock[i]);
            time[i] = new timeclick();
            time[i].setEditable(false);
            time[i].setFont(new Font("宋体",Font.BOLD,18));
            mainframe.add(time[i]);
            time[i].setBorder(new EmptyBorder(0,0,0,0));
            clock[i].setVisible(false);
            time[i].setVisible(false);
        }
        clock[0].setBounds((mfwidth+spwidth)/2,mfheight-70-cardheight-clocklength,clocklength,clocklength);
        clock[1].setBounds(200,cardheight,clocklength,clocklength);
        clock[2].setBounds(mfwidth-200-cardwidth-clocklength,cardheight,clocklength,clocklength);
        time[0].setBounds((mfwidth+spwidth)/2+clocklength,mfheight-70-cardheight-clocklength+20,125,30);
        time[1].setBounds(200+clocklength,cardheight+20,125,30);
        time[2].setBounds(mfwidth-200-cardwidth,cardheight+20,125,30);
        //
        mainframe.pack();
        mainframe.setVisible(true);
        mainframe.setResizable(false);
        mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         myhandler.handleAction(0);
        //********************************************************************************************
        //初始化界面
        int testcard[] = new int[17];
        for(int i=0;i<17;i++){//生成手牌测试数据
            testcard[i] = i;
        }
        mycard = testcard;

    }
    private void setChupaiqu(int i,int cards[],int[] b){//出牌区
        chupaiqu[i].setpanel(i, cards, b);
        new Thread(chupaiqu[i]).start();
    }
    public void setChupaiqu(int i, int cards[], int k){//出牌区
        chupaiqu[i].setpanel(i, cards, k);
        new Thread(chupaiqu[i]).start();
    }
    private Image[] setoutcards(int a[]){
        Image outcards[] = new Image[a.length];
        for(int i=0;i<a.length;i++){
            outcards[i] = turntocard(a[i]).getImage();//.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        }
        return outcards;
    }
    public void setMycard(int a[]){//玩家手牌
        shoupai[0].removeAll();
        int gap = 25;
        int len = a.length;
        int width = cardwidth+(len-1)*gap;
        int x = (spwidth+width)/2-cardwidth;
        card = new Card[len];
        for(int i=len-1;i>=0;i--){
            card[i] = new Card(turntocard(a[i]));
            card[i].number = a[i];
            shoupai[0].add(card[i]);
            card[i].setBounds(x,20,cardwidth,cardheight);
            x -= gap;
        }
        initlisten();
    }
    private void initmycard(int[] a){
        int gap = 25;
        int len = a.length;
        int width = cardwidth+(len-1)*gap;
        int x = (spwidth+width)/2-cardwidth;
        card = new Card[len];
        for(int i=len-1;i>=0;i--){
            card[i] = new Card(turntocard(a[i]));
            card[i].number = a[i];
            shoupai[0].add(card[i]);
            card[i].setVisible(false);
            card[i].setBounds(x,20,cardwidth,cardheight);
            x -= gap;
        }
        initlisten();
        new setbottom().start();
    }
    private void initlisten(){
        for (Card tag:card){
            tag.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    tag.chosen = true;
                    cardactive();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    cardchosen();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if(tag.canbechose){
                        tag.chosen = true;
                    }
                }
            });
        }
    }
    private void setothersShoupai(int i,int k){//i=1左侧，i=2右侧,k=手牌数量
        int gap = 15;
        for(int j=k-1;j>=0;j--){
            others[i-1][j] = new JLabel(back);
            shoupai[i].add(others[i-1][j]);
            others[i-1][j].setBounds(0,j*gap,cardwidth,cardheight);
        }
        cardsnumber[i-1].setText("手牌："+k+"张");
    }
    private void initleftcard(int k){
        int gap = 15;
        for(int j=k-1;j>=0;j--){
            others[0][j] = new JLabel(back);
            shoupai[1].add(others[0][j]);
            others[0][j].setVisible(false);
            others[0][j].setBounds(0,j*gap,cardwidth,cardheight);
        }
        new setleft().start();
    }
    private void initrightcard(int k){
        int gap = 15;
        for(int j=k-1;j>=0;j--){
            others[1][j] = new JLabel(back);
            shoupai[2].add(others[1][j]);
            others[1][j].setVisible(false);
            others[1][j].setBounds(0,j*gap,cardwidth,cardheight);
        }
        new setright().start();
    }
    private ImageIcon turntocard(int k){
        if(k<54)
        if(k==53){
            return bigjoker;
        }else if(k==52){
            return littlejoker;
        }else{
            int a = k/13;
            int b = k%13;
            switch (a){
                case 0:
                    return spade[b];
                case 1:
                    return heart[b];
                case 2:
                    return club[b];
                case 3:
                    return diamond[b];
            }
        }
        return back;
    }
    private void cardchosen(){
        for(Card tag:card){
            tag.updown();
            tag.canbechose = false;
        }
    }
    private void cardactive(){
        for(Card tag:card){
            tag.canbechose = true;
        }
    }
    private void setChupai(){//出牌按钮----------------------------------------------------------------------------------
        chupai.addActionListener(e -> {
            myhandler.handleAction(3);
        });
    }
    public void Chupai(){
        int a[] = getchosencards();
        int b[] = getremaincards();
        if(a.length!=0) {
            clock[0].buchu = false;
            clockstop(0);
            setChupaiqu(0, a, b);
        }
    }
    private void setBuchu(){//不出按钮
        buchu.addActionListener(e -> {
            myhandler.handleAction(4);
            clockstop(0);
            cardsdown();
        });
    }
    public int[] getchosencards(){
        ArrayList tmp = new ArrayList();
        for(Card tag:card) {
            if (tag.isup) {
                tmp.add(tag.number);
            }
        }
        int a[] = new int[tmp.size()];
        for(int i=0;i<tmp.size();i++){
            a[i] = (int)tmp.get(i);
        }
        return a;
    }
    private int[] getremaincards(){
        ArrayList tmp = new ArrayList();
        for(Card tag:card){
            if(!tag.isup){
                tmp.add(tag.number);
            }
        }
        int b[] = new int[tmp.size()];
        for(int i=0;i<tmp.size();i++){
            b[i] = (int)tmp.get(i);
        }
        return b;
    }
    private void cardsdown(){
        for(Card tag:card){
            if(tag.isup){
                tag.chosen=true;
                tag.updown();
            }
        }
    }
    private void setclock(int a){
        clock[a].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clockstart(a);
            }
        });
    }
    private void clockstart(int a){//计时器开始
        clock[a].setVisible(true);
        clock[a].stopped = false;
        time[a].setVisible(true);
        time[a].stopped = false;
        new Thread(time[a]).start();
        new Thread(clock[a]).start();
    }
    private void clockstop(int a){//计时器停止
        clock[a].stopped = true;
        time[a].stopped = true;
    }
    private void hidechupai(){
        chupai.setVisible(false);
        chupai.setEnabled(false);
        buchu.setVisible(false);
        buchu.setEnabled(false);
        mainframe.repaint();
    }
    private void showchupai(){
        chupai.setVisible(true);
        chupai.setEnabled(true);
        buchu.setVisible(true);
        buchu.setEnabled(true);
    }
    private void hidelordscore(){
        for(JButton b:lordscore){
            b.setVisible(false);
            b.setEnabled(false);
        }
    }
    private void showlordscore(int k){
        lordscore[0].setVisible(true);
        lordscore[0].setEnabled(true);
        for(int i=k+1;i<=3;i++){
            lordscore[i].setVisible(true);
            lordscore[i].setEnabled(true);
        }
    }
    private void hidepaiku(){
        paiku.setVisible(false);
    }
    private void setLordscore(){
        //初始化叫分按钮
        lordscore[0].addActionListener(e -> {
            myhandler.handleAction(5);
            clock[0].score = 0;
            clockstop(0);
        });
        lordscore[1].addActionListener(e -> {
            myhandler.handleAction(6);
            clock[0].score = 1;
            clockstop(0);
        });
        lordscore[2].addActionListener(e -> {
            myhandler.handleAction(7);
            clock[0].score = 2;
            clockstop(0);
        });
        lordscore[3].addActionListener(e -> {
            myhandler.handleAction(8);
            clock[0].score = 3;
            clockstop(0);
        });
    }
    private void showbuchutext(int a){
        buchusign[a].setText("不出");
        buchusign[a].setVisible(true);
    }
    private void showlordscore(int a,int score){
        buchusign[a].setText(getlordscore(score));
        buchusign[a].setVisible(true);
    }
    private void setLordsign(int a){
        switch (a){
            case 0:
                lordsign.setBounds((mfwidth-spwidth)/2-60,mfheight-85-cardheight,60,66);
                break;
            case 1:
                lordsign.setBounds(50+cardwidth,70,60,66);
                break;
            case 2:
                lordsign.setBounds(mfwidth-125-cardwidth,70,60,66);
                break;
        }
    }
    private String getlordscore(int score){
        switch (score){
            case 0:return "不叫";
            case 1:return "1分";
            case 2:return "2分";
            case 3:return "3分";
        }
        return "不叫";
    }
    private JLabel rocket(){
        ImageIcon rocketimg = new ImageIcon("rocket.png");
        JLabel rocket = new JLabel(){
            @Override
            public void paint(Graphics g){
                super.paint(g);
                rocketimg.paintIcon(this,g,0,0);
            }
        };
        mainframe.add(rocket);
        return rocket;
    }
    //------------------------------------------------------------------------------------------------------------------
    //重写Jlabel实现时钟
    public class Clock extends JLabel implements Runnable{
        boolean stopped = false;
        boolean buchu = true;
        boolean calllord = false;
        int score = 0;
        int a;
        Clock(ImageIcon icon){
            this.setIcon(icon);
        }
        @Override
        public void run() {
            buchusign[a].setVisible(false);
            for(int i=30;i>0;i--) {
                if (stopped)
                    break;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    break;
                }
                if(stopped)
                    break;
                try{
                    Thread.sleep(500);
                }catch (Exception e){
                    break;
                }
            }
            this.setVisible(false);
            hidechupai();
            hidelordscore();
            if(buchu)
                showbuchutext(a);
            buchu = true;
            if(calllord)
                showlordscore(a,score);
            calllord = false;
        }
    }
    //使用内部类重写了Jpanel方法，实现出牌动画
    public class Mypanel extends JPanel implements Runnable{
        JLabel[] cards;
        int b[];
        int a;
        int gap;
        int distance;
        int k;
        void setpanel(int a,int[] card,int[] b){
            cards = new JLabel[card.length];
            for(int i=card.length-1;i>=0;i--){
                cards[i] = new JLabel(turntocard(card[i]));
                this.add(cards[i]);
            }
            this.a = a;
            this.b = b;
        }
        void setpanel(int a,int[] card,int k){
            this.removeAll();
            cards = new JLabel[card.length];
            for(int i=card.length-1;i>=0;i--){
                cards[i] = new JLabel(turntocard(card[i]));
                this.add(cards[i]);
            }
            this.a = a;
            this.k = k;
        }
        private void drawcards(){
            switch (a){
                case 0:
                    drawMycards(cards,gap,distance);
                    break;
                case 1:
                    drawleftcards(cards,gap,distance);
                    break;
                case 2:
                    drawrightcards(cards,gap,distance);
                    break;
            }
        }
        private void drawMycards(JLabel card[],int gap,int distance){
            int width = (card.length-1)*gap+cardwidth;
            for (int i=0;i<card.length;i++){
                cards[i].setBounds((getWidth()-width)/2+i*gap,getHeight()-distance, cardwidth,cardheight);
            }
        }
        private void drawleftcards(JLabel card[],int gap,int distance){
            for (int i=0;i<card.length;i++){
                if(i<10)
                    cards[i].setBounds(distance+i*gap,0,cardwidth,cardheight);
                else cards[i].setBounds(distance+(i-10)*gap,50, cardwidth,cardheight);
            }
        }
        private void drawrightcards(JLabel card[],int gap,int distance){
            int width = (card.length-1)*gap+cardwidth;
            for (int i=0;i<card.length;i++){
                if(i<10)
                    cards[i].setBounds(this.getWidth()-width-distance+i*gap,0, cardwidth,cardheight);
                else cards[i].setBounds(this.getWidth()-width-distance+(i-10)*gap,50, cardwidth,cardheight);
            }
        }
        public void run(){
            setremaincards(a);
            shoupai[a].repaint();
            setdistance(a);
            double tag = gap;
            for(int i=distance;distance<=i+150;distance+=3){
                try{
                    Thread.sleep(10);
                }catch (Exception e){
                    break;
                }
                drawcards();
                tag+=0.5;
                gap=(int)tag;
            }
        }
        private void setdistance(int a){
            gap = 0;
            switch (a){
                case 0:
                    distance = 170;
                    break;
                case 1:
                    distance = 0;
                    break;
                case 2:
                    distance = 0;
                    break;
            }
        }
        private void setremaincards(int a){
            if(a==0)
                setMycard(b);
            else
                setothersShoupai(a,k);
        }
    }
    //火箭动画
    public class setrocket extends Thread{
        @Override
        public void run() {
            double a = 0.125;
            int width = 158;
            int height = 506;
            int x = (mfwidth-width)/2;
            int distance = 0;
            double speed = 0.5*a;
            for(int t=0;t<150;t++){
                distance+=speed;
                speed+=a;
                rocket.setBounds(x,mfheight-distance,width,height);
                try {
                    Thread.sleep(10);
                }catch (Exception e){
                    break;
                }
            }
        }
    }
    public void startRocket(){
        new setrocket().start();
    }
    //初始化发牌动画
    public class settop extends Thread{
        JLabel p1 = new JLabel(turntocard(lordcard[0]));
        JLabel p2 = new JLabel(turntocard(lordcard[1]));
        JLabel p3 = new JLabel(turntocard(lordcard[2]));
        public void run(){
            showlordcard.add(p1);
            showlordcard.add(p2);
            showlordcard.add(p3);
            for(int gap=0;gap<=122;gap+=2){
                try{
                    Thread.sleep(15);
                }catch (Exception e){
                    break;
                }
                drawlordcard(gap);
            }
        }
        private void drawlordcard(int gap){
            p1.setBounds(122-gap,0,cardwidth,cardheight);
            p2.setBounds(122,0,cardwidth,cardheight);
            p3.setBounds(122+gap,0,cardwidth,cardheight);
        }
    }
    public class setbottom extends Thread{
        public void run(){
            for(Card tag:card){
                try{
                    Thread.sleep(200);
                }catch (Exception e){
                    break;
                }
                tag.setVisible(true);
            }
            hidepaiku();
            new settop().start();//地主牌展示
        }
    }
    public class setleft extends Thread{
        public void run(){
            for(int i=0;i<otherscards[0];i++){
                try{
                    Thread.sleep(200);
                }catch (Exception e){
                    break;
                }
                others[0][i].setVisible(true);
                cardsnumber[0].setText("手牌"+(i+1)+"张");
            }
        }
    }
    public class setright extends Thread{
        public void run(){
            for(int i=0;i<otherscards[1];i++){
                try{
                    Thread.sleep(200);
                }catch (Exception e){
                    break;
                }
                others[1][i].setVisible(true);
                cardsnumber[1].setText("手牌"+(i+1)+"张");
            }
        }
    }
    //接口
    public void setlord(int a){
        whoislord = a;
        setLordsign(a);
        for(JTextField i:buchusign)
            i.setVisible(false);
        if(a==0){
            setMycard(mycard);
        }
        else {
            otherscards[a-1]+=3;
            setothersShoupai(a,otherscards[a-1]);
        }
    }
    public void startturn(int a){
        if(a==0)
            showchupai();
        chupaiqu[a].removeAll();
        mainframe.repaint();
        clockstart(a);
    }
    public void startcalllord(int a,int k){
        if(a==0)
            showlordscore(k);
        clock[a].calllord = true;
        clockstart(a);
    }
    public void calllordscore(int a,int k){
        clock[a].score = k;
        clock[a].stopped = true;
    }
    public void startgame(){
        buchusign[0].setVisible(false);
        initmycard(mycard);//玩家手牌
        initleftcard(otherscards[0]);//其他两个玩家手牌
        initrightcard(otherscards[1]);
        setChupai();
        setBuchu();
        hidechupai();
        setLordscore();
        for(int i=0;i<3;i++){
            setclock(i);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        maininterface mf = new maininterface();
    }
}
