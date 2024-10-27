package BS.mapper;

import BS.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ContactMapper extends BaseMapper<Contact> {
    Contact selectByContactName(String contactName);
    List<Contact> selectContacts(Contact contact);
    Integer selectContactsCount(Contact contact);
}




