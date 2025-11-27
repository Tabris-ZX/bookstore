package schoolwork.bookstore.util;

import com.github.yitter.idgen.YitIdHelper;

public class Build {

    public static long buildUid(){
        long uid = YitIdHelper.nextId()%100000000;
        while(uid<1e7){
            uid = YitIdHelper.nextId()%100000000;
        }
        return uid;
    }
//
//    public static long buildBid(){
//        long bid = YitIdHelper.nextId()%100000000;
//        while(bid<1e7){
//            bid = YitIdHelper.nextId()%100000000;
//        }
//        return bid;
//    }
}
