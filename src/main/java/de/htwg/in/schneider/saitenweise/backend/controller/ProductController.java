package de.htwg.in.schneider.saitenweise.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.htwg.in.schneider.saitenweise.backend.model.Category;
import de.htwg.in.schneider.saitenweise.backend.model.Product;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @GetMapping
    public List<Product> getProducts() {
        Product p1 = new Product();
        p1.setId(1);
        p1.setTitle("IT-Landschaftscheck");
        p1.setDescription("Ihre aktuell verwendete IT-Landschaft und verwendeten Programme werden von unseren Experten analysiert und geprüft. Hier werden mögliche Schnittstellen identifiziert, die Ihr Potenzial maximieren.");
        p1.setCategory(Category.PACKAGES);
        p1.setPrice(500.0);
        //p1.setImageUrl("https://neshanjo.github.io/saitenweise-images/violin_pro.jpg");

        Product p2 = new Product();
        p2.setId(2);
        p2.setTitle("Mockup-Erstellung 4h");
        p2.setDescription("Wir erstellen ein interaktives Mockup Ihrer zukünftigen Webanwendung, um Design und Funktionalität zu visualisieren.");
        p2.setCategory(Category.FRONTEND);
        p2.setPrice(400.0);

        Product p3 = new Product();
        p3.setId(10);
        p3.setTitle("Projektmanagement Coaching 60min");
        p3.setDescription("Optimieren Sie Ihre Präsentationsfähigkeiten mit unserem Projektmanagement Coaching, um Investoren und Kunden zu überzeugen.");
        p3.setCategory(Category.COACHING);
        p3.setPrice(120.0);

        Product p4 = new Product();
        p4.setId(200);
        p4.setTitle("Anwendertraining 120min");
        p4.setDescription("Intensives Training für Anwender, um die Nutzung unserer Software effektiv zu gestalten.");
        p4.setCategory(Category.COACHING);
        p4.setPrice(200.0);

        return Arrays.asList(p1, p2, p3, p4);
    }
}