package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import schoolwork.bookstore.model.Order;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
