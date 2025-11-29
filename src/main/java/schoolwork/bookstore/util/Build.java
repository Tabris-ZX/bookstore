package schoolwork.bookstore.util;

import com.github.yitter.idgen.YitIdHelper;

public class Build {

    public static long buildUid(){
        return 10_000_000+YitIdHelper.nextId()%10_000_000;
    }
//
    public static long buildBid(){
        return 100_000 + YitIdHelper.nextId()%100_000;
    }
}
