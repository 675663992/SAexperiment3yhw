package BS.controller;

import BS.entity.Contact;
import BS.service.ContactService;
import cn.dev33.satoken.util.SaResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ContactController {
    @Autowired
    private ContactService contactService;
    @GetMapping("/contact")
    public SaResult queryGoodInfo(Contact contact){
        return contactService.queryContactInfos(contact);
    }
    @DeleteMapping("/contact/{contactId}")
    public SaResult removeContact(@PathVariable("contactId") String contact){
        return contactService.removeContact(contact);
    }

    @PostMapping("/contact")
    public SaResult addContact(@RequestBody Contact contact){
        return contactService.addContact(contact);
    }

    @GetMapping("/contact/{contactName}")
    public SaResult queryContactByGoodName(@PathVariable("contactName") String contactName){
        return contactService.queryContactNameIsExist(contactName);
    }

    @PutMapping("/contact")
    public SaResult updateContact(@RequestBody Contact contact){
        return contactService.updateContact(contact);
    }
}
