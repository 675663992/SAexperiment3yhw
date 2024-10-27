package BS.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@TableName(value = "contacts")
@Data
public class Contact {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String name;
    private String address;
    private String phone;
}
