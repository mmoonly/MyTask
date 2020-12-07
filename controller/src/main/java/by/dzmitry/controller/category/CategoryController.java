package by.dzmitry.controller.category;

import by.dzmitry.service.category.CategoryService;
import by.dzmitry.service.dto.AdvertDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Api(tags = "Categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ApiOperation("Used to add parent categories.")
    public void addCategory(@RequestParam String thread) {
        categoryService.addCategory(thread);
    }

    @PostMapping("/{id}")
    @ApiOperation("Used to add categories with parent.")
    public void addCategoryWithParent(@PathVariable Integer id, @RequestParam String thread) {
        categoryService.addCategoryWithParent(id, thread);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Used to delete categories.")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Used to modify categories.")
    public void modifyCategory(@PathVariable Integer id, @RequestParam String thread) {
        categoryService.modifyCategory(id, thread);
    }

    @GetMapping("/{id}/adverts")
    @ApiOperation("Used to get adverts by categories.")
    public ResponseEntity<List<AdvertDto>> getAdverts(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.getAdvertsByCategory(id));
    }
}
