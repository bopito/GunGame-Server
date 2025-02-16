package server.entity;

public class Item extends Entity {
    
    private String type; // 

    public Item(String type) {
        super();
        this.type = type;
        
    }

    public String getType() {
        return type;
    }


    @Override
    public void update() {
        angle += 1;
    }
}
