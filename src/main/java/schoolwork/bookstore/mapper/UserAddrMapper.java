package schoolwork.bookstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import schoolwork.bookstore.model.UserAddr;

import java.util.Map;

@Mapper
public interface UserAddrMapper extends BaseMapper<UserAddr> {

}
