import java.lang.reflect.Field

import scala.collection.immutable.Stream.Empty
import scala.collection.mutable.ListBuffer

object Application {

    trait Kebab {
        def isVegetarian: Boolean
        def isPescetarian: Boolean
        def removeOnions():Kebab
        def doubleCheese():Kebab

    }

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

    case class InheritenceKebab(ingredients: List[Ingredient]) extends Kebab {
        val isVegetarian: Boolean = ingredients.forall(_.isVegeratian)
        val isPescetarian: Boolean = ingredients.forall(_.isPescetarian)

        def removeOnions(): Kebab = InheritenceKebab(ingredients.filterNot(_.name == ognonName))

        def doubleCheese: InheritenceKebab = {
            var ingredientsNew: ListBuffer[Ingredient] = new ListBuffer[Ingredient]()
            ingredients.foreach {
                ingredient => {
                    ingredientsNew += ingredient
                    if (ingredient.name == "cheese")
                        ingredientsNew += ingredient.copy()

                }
            }
            InheritenceKebab(ingredientsNew.toList)
        }

        def ingredientsList: List[String] = ingredients.map(_.name)

        override def toString: String = ingredientsList.mkString(", ")
    }

    // ------------------------------------------------------------------- Composite pattern version
    class CompositeKebab(name: String, next: CompositeKebab) extends Kebab {
        def isVegetarian: Boolean = next.isVegetarian

        def isPescetarian: Boolean = next.isPescetarian

        def ingredientsList: List[String] = name :: next.ingredientsList

        override def toString: String = {
            //            val nextString = next.toString
            //            // Implode with "," until empty
            //            name + (
            //                nextString match {
            //                    case empty if nextString.isEmpty => empty
            //                    case _ => ", " + nextString
            //                })

            ingredientsList.mkString(", ")
        }

        def copy(name: String = name, next: CompositeKebab = next) = CompositeKebab(name, next)

        override def removeOnions(): Kebab = ???

        override def doubleCheese(): Kebab = ???
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

        override def ingredientsList: List[String] = Nil
    }

}