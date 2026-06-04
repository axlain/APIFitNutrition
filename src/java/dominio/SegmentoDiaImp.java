package dominio;

import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.SegmentoDia;

public class SegmentoDiaImp {

    public static List<SegmentoDia> obtenerTodos() {
        List<SegmentoDia> segmentos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                segmentos = conexionBD.selectList("segmentoDia.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return segmentos;
    }
}
