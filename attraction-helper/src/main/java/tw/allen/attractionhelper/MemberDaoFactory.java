package tw.allen.attractionhelper;

import java.io.IOException;

import javax.imageio.IIOException;

public class MemberDaoFactory {
    public static IMemberDao createMemberDao() throws IOException {
        IMemberDao dao = new MemberDaoJdbcImpl();
        return dao;
    }
}
