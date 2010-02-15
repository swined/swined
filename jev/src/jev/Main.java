package jev;

public class Main {

    public static void main(String[] args) {
        EventDispatcher d = new EventDispatcher();
        I i = d.invoke(I.class);
        if (i == null)
            System.out.println("null j");
        i.testByte();
        i.testShort();
        i.testInt();
        i.testLong();
        i.testFloat();
        i.testDouble();
        i.testChar();
        i.testString();
        i.testBoolean();
        i.testVoid();
        i.testObject();
        i.testEnum();
        i.testArray();
    }

}
