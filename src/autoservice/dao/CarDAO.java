package autoservice.dao;

import autoservice.model.Car;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private static final String CarFile = "cars.csv";

    public List<Car> findAll() {
        List<Car> list = new ArrayList<>();
        File file = new File(CarFile);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] p = line.split(";", -1);
                if (p.length < 7) {
                    continue;
                }

                Car car = new Car(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), Integer.parseInt(p[4].trim()), p[5].trim(), p[6].trim());

                list.add(car);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return list;
    }

    public void save(Car car) {
        List<Car> all = findAll();

        if (car.getId() == 0) {
            car.setId(nextId(all));
            all.add(car);
        } else {
            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getId() == car.getId()) {
                    all.set(i, car);
                    break;
                }
            }
        }
        writeAll(all);
    }

    public void delete(int id) {
        List<Car> all = findAll();
        all.removeIf(c -> c.getId() == id);
        writeAll(all);
    }

    public List<Car> searchByBrand(String brand) {
        List<Car> result = new ArrayList<>();
        for (Car c : findAll())
            if (c.getBrand().toLowerCase().contains(brand.toLowerCase())) {
                result.add(c);
            }

        return result;
    }

    public List<Car> searchByOwner(String owner) {
        List<Car> result = new ArrayList<>();
        for (Car c : findAll())
            if (c.getOwnerName().toLowerCase().contains(owner.toLowerCase())) {
                result.add(c);
            }

        return result;
    }

    public List<Car> searchByPlate(String plate) {
        List<Car> result = new ArrayList<>();
        for (Car c : findAll())
            if (c.getLicensePlate().toLowerCase().contains(plate.toLowerCase())) {
                result.add(c);
            }

        return result;
    }

    private int nextId(List<Car> list) {
        int max = 0;
        for (Car c : list)
            if (c.getId() > max) {
                max = c.getId();
            }

        return max + 1;
    }

    private void writeAll(List<Car> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CarFile))) {
            for (Car c : list) {
                pw.println(c.getId() + ";" + c.getBrand() + ";" + c.getModel() + ";" + c.getLicensePlate() + ";" +
                        c.getYear() + ";" + c.getOwnerName() + ";" + c.getOwnerPhone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}