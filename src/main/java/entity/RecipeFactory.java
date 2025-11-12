package entity;

public class RecipeFactory {
    public Recipe create(String id, String name, String category, String area,
                         String instructions, String imageUrl, String youtubeUrl,
                         String sourceUrl, String[] ingredients, String[] measures) {
        return new Recipe(id, name, category, area, instructions, imageUrl, youtubeUrl, sourceUrl, ingredients, measures);
    }
}
