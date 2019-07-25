package tw.allen.attractionhelper;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AttractionDaoFactory {
    public static IAttractionDao createAttractionDao() throws FileNotFoundException, IOException {
        IAttractionDao dao = new AttractionDaoJdbcImpl();
        return dao;
    }
}
