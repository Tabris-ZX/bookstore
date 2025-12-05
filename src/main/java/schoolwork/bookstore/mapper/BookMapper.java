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

    @Insert("INSERT ignore INTO books(bid,ISBN,title, author, publisher,books.description,books.cover_url,rating,price) " +
            "VALUES(#{bid},#{isbn},#{title}, #{author},#{publisher},#{desc},#{coverUrl},#{rating},#{price})")
    void addBookByJson(long bid,String isbn,String title,String author,String publisher,String desc,String coverUrl,int rating,double price);

    @Select("SELECT isbn FROM books")
    List<String> getBooksISBN();

    @Update("UPDATE books set sales=sales+#{number},stock = stock - #{number} where bid = #{bid} and stock >= #{number}")
    int solveSold(@Param("bid") long bid, @Param("number") int number);

    @Select("SELECT stock >= #{number} FROM books WHERE bid = #{bid}")
    boolean checkStock(@Param("bid") long bid, @Param("number") int number);


    @Update("UPDATE books set description = #{desc} WHERE isbn = #{isbn}")
    void changeDesc(String isbn,String desc);


    @Select("SELECT * FROM books ")
    List<AIData> getBookInfoForAI();



}
