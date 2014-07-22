package HelsinkiKanavaDataAccess;

public class Attendance implements Comparable<Attendance>
{
    public String name;
    public String party;
    public String seat;

    @Override
    public int compareTo(Attendance another)
    {
        return party.compareTo(another.party);
    }
}
