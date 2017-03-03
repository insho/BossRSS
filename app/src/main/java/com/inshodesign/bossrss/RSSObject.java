package com.inshodesign.bossrss;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by JClassic on 3/3/2017.
 */

public class RSSObject {

    @Root(name = "breakfast_menu")
    public class BreakFastMenu {
        @ElementList(inline = true)
        List<Food> foodList;
    }

    @Root(name="food")
    public class Food {
        @Element(name = "name")
        String name;

        @Element(name = "price")
        String price;

        @Element(name = "description")
        String description;

        @Element(name = "calories")
        String calories;
    }

}
