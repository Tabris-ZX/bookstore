package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
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

}
