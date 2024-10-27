package BS.service.impl;

import BS.entity.Contact;
import BS.service.ContactService;
import BS.mapper.ContactMapper;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GontactServiceImpl extends ServiceImpl<ContactMapper, Contact>
        implements ContactService {
    @Autowired
    private ContactMapper ContactMapper;
    @Override
    public SaResult queryContactInfos(Contact contact) {
        List<Contact> Contacts=ContactMapper.selectContacts(contact);
        Integer count=ContactMapper.selectContactsCount(contact);
        Map<String,Object> map =new HashMap<>();
        map.put("ContactInfos",Contacts);
        map.put("totals",count);
        return SaResult.get(200,"查询成功",map);
    }

    @Override
    public SaResult queryContactNameIsExist(String contactName) {
        Contact contact = ContactMapper.selectByContactName(contactName);
        if(contact!=null){
            return SaResult.get(402,"该联系人已存在",null);
        }else{
            return SaResult.ok("该联系人不存在");
        }
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public SaResult removeContact(String ContactId) {
        int num=ContactMapper.deleteById(ContactId);
        return SaResult.ok("删除成功！");
    }

    @Override
    public SaResult addContact(Contact contact) {
        int insert=ContactMapper.insert(contact);
        return SaResult.ok("添加成功！");
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public SaResult updateContact(Contact contact) {

        UpdateWrapper<Contact> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",contact.getId());

        int update = ContactMapper.update(contact, updateWrapper);
        return SaResult.ok("修改成功！");
    }
}



