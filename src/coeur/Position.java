package coeur;

public class Position {

    private int x=0,y=0;

    public Position(int x,int y){
        this.x = x;
        this.y =y;
    }


    //getters
    public int getX(){return this.x;}
    public int getY(){return this.y;}

    //setters
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}

    //ajouter une position avec une instance de Position
    public void ajouter(Position p){
        this.x += p.getX();
        this.y += p.getY();
    }

    //ajouter une position avec des coordonners x & y
    public void ajouter(int x, int y){
        this.x +=x;
        this.y += y;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Position)) return false;
        Position p = (Position) obj;

        return (p.getX() == this.x) && (p.getY() == this.y);
    }
}