package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import schoolwork.bookstore.dto.AIData;
import schoolwork.bookstore.model.Book;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {

    @Select("SELECT * FROM books")
    List<Book> getAllBooksInfo();

    @Select("SELECT * FROM books WHERE stock > 0")
    List<Book> getStockBooks();

    @Update("UPDATE books SET stock = #{curStock} WHERE bid = #{bid}")
    boolean updateBookStock(@Param("bid") long bid,@Param("curStock") int curStock);

    @Insert("INSERT INTO books(bid,title, author, publish) " +
            "VALUES(#{bid},#{title}, #{author},#{publish})")
    void addBookByJson(long bid,String title,String author,String publish);

    @Update("UPDATE books set sales=sales+#{number},stock = stock - #{number} where bid = #{bid} and stock >= #{number}")
    int solveSold(@Param("bid") long bid, @Param("number") int number);

    @Select("SELECT stock >= #{number} FROM books WHERE bid = #{bid}")
    boolean checkStock(@Param("bid") long bid, @Param("number") int number);

    @Select("SELECT * FROM books ")
    List<AIData> getBookInfoForAI();

}
