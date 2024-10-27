package ThreeCS;
import java.util.List;

public class BusinessLogic {
    private DataAccess dataAccess = new DataAccess();

    public void addContact(String name, String address, String phone) {
        dataAccess.addContact(name, address, phone);
    }

    public void updateContact(int id, String name, String address, String phone) {
        dataAccess.updateContact(id, name, address, phone);
    }

    public void deleteContact(int id) {
        dataAccess.deleteContact(id);
    }

    public List<DataAccess.Contact> getAllContacts() {
        return dataAccess.getAllContacts();
    }
}
