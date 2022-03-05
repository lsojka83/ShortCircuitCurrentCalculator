package pl.ls4soft.sscc;


public class Option implements Comparable<Option> {

    private String name;
    private String date;
    private String path;

    public Option (String n, String d, String p)
    {
        name = n;
        date = d;
        path = p;
    }

    public String getName()
    {
        return name;
    }

    public String getData()
    {
        return date;
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public int compareTo(Option o) {
        if(this.name != null)
//        return 0;
        return name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();

    }
}
