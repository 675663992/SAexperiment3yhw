package BS.service;

import BS.entity.Contact;
import cn.dev33.satoken.util.SaResult;

public interface ContactService {
    SaResult queryContactInfos(Contact contact);
    SaResult removeContact(String contactId);
    SaResult addContact(Contact contact);
    SaResult queryContactNameIsExist(String contactName);
    SaResult updateContact(Contact contact);
}
