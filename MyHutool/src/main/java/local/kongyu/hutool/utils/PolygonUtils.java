package local.kongyu.hutool.utils;

import com.alibaba.fastjson2.JSONArray;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 点 线 面的交差
 *
 * @author 孔余
 * @since 2023-10-20 09:32
 */
public class PolygonUtils {
    // 判断折线是否与多边形有交集
    public static boolean isPolylineIntersectPolygon(JSONArray polygonArray, JSONArray polylineArray) {
        List<Point2D.Double> polygonVertices = parsePolygonVertices(polygonArray);
        List<Point2D.Double> polylineVertices = parsePolylineVertices(polylineArray);

        // 检查折线的每一段是否与多边形的边相交
        for (int i = 0; i < polylineVertices.size() - 1; i++) {
            Point2D.Double lineStart = polylineVertices.get(i);
            Point2D.Double lineEnd = polylineVertices.get(i + 1);

            for (int j = 0; j < polygonVertices.size(); j++) {
                Point2D.Double polygonStart = polygonVertices.get(j);
                Point2D.Double polygonEnd = polygonVertices.get((j + 1) % polygonVertices.size());

                if (doLineSegmentsIntersect(lineStart, lineEnd, polygonStart, polygonEnd)) {
                    return true;
                }
            }
        }

        // 检查折线的任意一点是否在多边形内部
        for (Point2D.Double point : polylineVertices) {
            if (isPointInPolygon(polygonArray, point.getX(), point.getY())) {
                return true;
            }
        }

        return false;
    }

    // 判断两条线段是否相交
    private static boolean doLineSegmentsIntersect(Point2D.Double p1, Point2D.Double p2,
                                                   Point2D.Double q1, Point2D.Double q2) {
        return isCounterClockwise(p1, q1, q2) != isCounterClockwise(p2, q1, q2) &&
                isCounterClockwise(p1, p2, q1) != isCounterClockwise(p1, p2, q2);
    }

    // 辅助函数：判断三点是否为逆时针
    private static boolean isCounterClockwise(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    // 解析折线顶点
    public static List<Point2D.Double> parsePolylineVertices(JSONArray polylineArray) {
        List<Point2D.Double> vertices = new ArrayList<>();
        for (int i = 0; i < polylineArray.size(); i++) {
            JSONArray vertexData = polylineArray.getJSONArray(i);
            double vertexX = vertexData.getDouble(0);
            double vertexY = vertexData.getDouble(1);
            vertices.add(new Point2D.Double(vertexX, vertexY));
        }
        return vertices;
    }

    public static boolean isPointInPolygon(JSONArray polygonArray, double x, double y) {
        Path2D.Double polygon = new Path2D.Double();

        List<Point2D.Double> vertices = parsePolygonVertices(polygonArray);

        for (int i = 0; i < vertices.size(); i++) {
            Point2D.Double vertex = vertices.get(i);
            double vertexX = vertex.getX();
            double vertexY = vertex.getY();

            if (i == 0) {
                polygon.moveTo(vertexX, vertexY);
            } else {
                polygon.lineTo(vertexX, vertexY);
            }
        }

        polygon.closePath();

        return polygon.contains(x, y);
    }

    public static List<Point2D.Double> parsePolygonVertices(JSONArray polygonArray) {
        List<Point2D.Double> vertices = new ArrayList<>();
        for (int i = 0; i < polygonArray.size(); i++) {
            JSONArray vertexData = polygonArray.getJSONArray(i);
            double vertexX = vertexData.getDouble(0);
            double vertexY = vertexData.getDouble(1);
            vertices.add(new Point2D.Double(vertexX, vertexY));
        }
        return vertices;
    }
}

