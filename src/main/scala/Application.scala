import java.lang.reflect.Field

import scala.collection.immutable.Stream.Empty
import scala.collection.mutable.ListBuffer

object Application {


    val ognonName = "ognon"

    var ingredients: List[Ingredient] = List()

    trait IngredientTrait {
        val name: String
    }

    case class Ingredient(override val name: String, hasMeat: Boolean = false, hasFish: Boolean = false) extends IngredientTrait {

        override def toString: String = name

        val isVegeratian: Boolean = !hasMeat && !hasFish
        val isPescetarian: Boolean = !hasMeat
    }

    case class Kebab(ingredients: List[Ingredient]) {
        val isVegetarian: Boolean = ingredients.forall(_.isVegeratian)
        val isPescetarian: Boolean = ingredients.forall(_.isPescetarian)

        override def toString: String = ingredients.map(_.name).mkString(", ")

        def removeOnions() = Kebab(ingredients.filterNot(_.name == ognonName))

        def doubleCheese: Kebab = {
            var ingredientsNew: ListBuffer[Ingredient] = new ListBuffer[Ingredient]()
            ingredients.foreach {
                ingredient => {
                    ingredientsNew += ingredient
                    if (ingredient.name == "cheese")
                        ingredientsNew += ingredient.copy()

                }
            }
            Kebab(ingredientsNew.toList)
        }
    }

    // ------------------------------------------------------------------- Composite pattern version
    class CompositeKebab(name: String, next: CompositeKebab) {
        def isVegetarian: Boolean = next.isVegetarian
        def isPescetarian: Boolean = next.isPescetarian

        override def toString: String = {
            val nextString = next.toString
            // Implode with "," until empty
            name + (
                nextString match {
                    case empty if nextString.isEmpty => empty
                    case _ => ", " + nextString
                })
        }
    }

    object CompositeKebab {
        def apply(name: String, next: CompositeKebab): CompositeKebab = new CompositeKebab(name, next)
    }

    case class MeatIngredient(name: String, next: CompositeKebab) extends CompositeKebab(name: String, next: CompositeKebab) {
        override def isVegetarian: Boolean = false
        override def isPescetarian: Boolean = false
    }

    case class FishIngredient(name: String, next: CompositeKebab) extends CompositeKebab(name: String, next: CompositeKebab) {
        override def isVegetarian: Boolean = false
    }

    // Null object
    object EmptyIngredient extends CompositeKebab(name = null, next = null) {
        override def isVegetarian: Boolean = true
        override def isPescetarian: Boolean = true
        override def toString: String = ""
    }
}