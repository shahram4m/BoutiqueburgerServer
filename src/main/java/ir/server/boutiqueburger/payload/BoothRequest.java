package ir.server.boutiqueburger.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class BoothRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(min = 2, max = 6)
    @Valid
    private List<ProductRequest> products;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }
}