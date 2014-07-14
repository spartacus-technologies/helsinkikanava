package HelsinkiKanavaDataAccess;

import java.util.List;
import java.util.Map;

/**
 * Created by erno on 12/07/14.
 */
public interface IHelsinkiKanavaDataAccess
{
    /* Get metadata for given session url */
    Metadata GetMetadata(String url);

    /* Get metadata for given session urls */
    Map<String /*url*/, Metadata> GetMetadatas(List<String> urls);

    /* Get all available sessions */
    Map<String /*url*/, String /*tittle*/> GetSessions();

    /*Note.
    * Loading all sessions' metadata is ~6.2 mB.
    * */
}
