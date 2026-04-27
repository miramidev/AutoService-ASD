package autoservice.dao;

import autoservice.model.Order;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String orderFile = "orders.csv";

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        File f = new File(orderFile);
        if (!f.exists()) return list;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(";", -1);
                if (p.length < 9) continue;

                Order order = new Order(
                        Integer.parseInt(p[0].trim()),
                        Integer.parseInt(p[1].trim()),
                        p[2].trim(),
                        p[3].trim(),
                        p[4].trim(),
                        Double.parseDouble(p[5].trim()),
                        p[6].trim(),
                        p[7].trim(),
                        Order.Status.valueOf(p[8].trim())
                );
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void save(Order order) {
        List<Order> all = findAll();

        if (order.getId() == 0) {
            order.setId(nextId(all));
            all.add(order);
        } else {
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == order.getId()) {
                    all.set(i, order);
                    break;
                }
            }
        }
        writeAll(all);
    }

    public void delete(int id) {
        List<Order> all = findAll();
        all.removeIf(o -> o.getId() == id);
        writeAll(all);
    }

    // Поиск по мастеру
    public List<Order> searchByMaster(String master) {
        List<Order> result = new ArrayList<>();
        for (Order o : findAll())
            if (o.getMasterName().toLowerCase().contains(master.toLowerCase()))
                result.add(o);
        return result;
    }

    // Поиск по статусу
    public List<Order> searchByStatus(Order.Status status) {
        List<Order> result = new ArrayList<>();
        for (Order o : findAll())
            if (o.getStatus() == status)
                result.add(o);
        return result;
    }

    // Поиск по дате создания
    public List<Order> searchByDate(String date) {
        List<Order> result = new ArrayList<>();
        for (Order o : findAll())
            if (o.getDateCreated().contains(date))
                result.add(o);
        return result;
    }

    private int nextId(List<Order> list) {
        int max = 0;
        for (Order o : list)
            if (o.getId() > max) max = o.getId();
        return max + 1;
    }

    private void writeAll(List<Order> list) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(orderFile), "UTF-8"))) {
            for (Order o : list) {
                pw.println(
                        o.getId() + ";" +
                                o.getCarId() + ";" +
                                o.getCarInfo() + ";" +
                                o.getDescription() + ";" +
                                o.getMasterName() + ";" +
                                o.getCost() + ";" +
                                o.getDateCreated() + ";" +
                                o.getDateCompleted() + ";" +
                                o.getStatus().name()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}