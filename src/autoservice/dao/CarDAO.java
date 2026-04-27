package autoservice.dao;

import autoservice.model.Car;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

                Car car = new Car(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(),
                        Integer.parseInt(p[4].trim()), p[5].trim(), p[6].trim());

                list.add(car);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return list;
    }
}