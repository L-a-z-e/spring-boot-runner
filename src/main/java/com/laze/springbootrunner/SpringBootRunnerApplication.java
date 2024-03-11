package com.laze.springbootrunner;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SpringBootRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRunnerApplication.class, args);
    }

}

@Entity
class Coffee {
    @Id
    private String id;
    private String name;

    public Coffee(){};
    public Coffee(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}

interface CoffeeRepository extends CrudRepository<Coffee, String>{};

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {
//    private List<Coffee> coffees = new ArrayList<>();

    private final CoffeeRepository coffeeRepository;
    public RestApiDemoController(CoffeeRepository coffeeRepository) {
////        coffees.addAll(List.of(
////                new Coffee("Americano"),
////                new Coffee("Tea"),
////                new Coffee("Latte"),
////                new Coffee("Juice")
////        ));
        this.coffeeRepository = coffeeRepository;
//        this.coffeeRepository.saveAll(List.of(
//                new Coffee("Americano"),
//                new Coffee("Tea"),
//                new Coffee("Latte"),
//                new Coffee("Juice")));
    }

//    @RequestMapping(value = "/coffees", method = RequestMethod.GET)
//    Iterable<Coffee> getCoffees() {
//        return coffees;
//    }
//    public DataLoader(CoffeeRepository coffeeRepository) {
//        this.coffeeRepository = coffeeRepository;
//    }

    @GetMapping("/")
    Iterable<Coffee> getCoffees() {
//        return coffees;
        return coffeeRepository.findAll();
    }

    @GetMapping("/{id}")
//    Optional<Coffee> getCoffeeById(@PathVariable String id) {
//        for (Coffee c: coffees) {
//            if (c.getId().equals(id)) {
//                return Optional.of(c);
//            }
//        }
//
//        return Optional.empty();
    Optional<Coffee> getCoffeeById(@PathVariable String id) {
        return coffeeRepository.findById(id);
    }

    @PostMapping("/")
    Coffee postCoffee(@RequestBody Coffee coffee) {
//        coffees.add(coffee);
//        return coffee;
        return coffeeRepository.save(coffee);
    }

    @PutMapping("/{id}")
    ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
//        int coffeeIndex = -1;
//
//        for (Coffee c : coffees) {
//            if(c.getId().equals(id)) {
//                coffeeIndex = coffees.indexOf(c);
//                coffees.set(coffeeIndex, coffee);
//            }
//        }

        // return (coffeeIndex == -1) ? postCoffee(coffee) : coffee;
        // postCoffee -> 201(Created) / putCoffee -> 200(OK)
//        return (coffeeIndex == -1) ? new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED) : new ResponseEntity<>(coffee, HttpStatus.OK);
        return (!coffeeRepository.existsById(id)) ? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED) : new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteCoffee(@PathVariable String id) {

//      coffees.removeIf(coffee -> coffee.getId().equals(id));
        coffeeRepository.deleteById(id);
    }

}
@Component
class DataLoader {
    private final CoffeeRepository coffeeRepository;
    public DataLoader(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @PostConstruct
    private void loadData() {
        coffeeRepository.saveAll(List.of(
                new Coffee("Americano"),
                new Coffee("Tea"),
                new Coffee("Latte"),
                new Coffee("Juice")));
    }
}