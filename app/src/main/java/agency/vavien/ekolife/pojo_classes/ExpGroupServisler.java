package agency.vavien.ekolife.pojo_classes;

import java.util.ArrayList;

/**
 * Created by SD on 5.02.2018.
 * dilmacsedat@gmail.com
 * :)
 */

public class ExpGroupServisler {
    private String Name;
    private ArrayList<ExpChildServisler> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<ExpChildServisler> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ExpChildServisler> Items) {
        this.Items = Items;
    }
}
