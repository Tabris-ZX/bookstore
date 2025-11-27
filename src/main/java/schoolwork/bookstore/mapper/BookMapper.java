package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.catalina.LifecycleState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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


}
