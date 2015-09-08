/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javajo.sample.data;

import javajo.sample.EvaluatingException;
import javajo.sample.data.MaybeBase.Nothing;
import javajo.sample.data.MaybeBase.Some;
import javajo.sample.functions.Functions.Condition;
import javajo.sample.functions.Functions.Function;
import javajo.sample.functions.Functions.Generator;
import javajo.sample.functions.Functions.Operator;
import javajo.sample.functions.Functions.Task;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

import static javajo.sample.functions.Functions.verifyNotNullObject;

public interface Maybe<V> {

    /**
     * Whether this container has non null value.
     * @return {@code true} when this container has non null value. {@code false} when this container has null value.
     */
    boolean isSome();

    /**
     * Whether this container doesn't have value.
     * @return {@code false} when this container has non null value. {@code true} when this container has null value.
     */
    default boolean isNothing() {
        return !isSome();
    }

    /**
     * Mapping object to another object.
     * @param fun - a mapping function.
     * @param <R> - result type
     * @return - Data container with mapped object. If mapping function returns null, the result container becomes {@link Nothing}.
     */
    <R> Maybe<R> map(Function<? super V, ? extends R> fun);

    /**
     * Mapping object to another object with a context.
     * @param fun - a mapping function.
     * @param <R> - result type.
     * @return - Data container with mapped object. If mapping function returns null, the result container becomes {@link Nothing}.
     */
    <R> Maybe<R> fmap(Function<? super V, ? extends Maybe<R>> fun);

    /**
     * Filtering object.
     * @param cond - a filtering condition.
     * @return - Data container with the same value. If the condition returns {@code false}, the result container becomes {@link Nothing}.
     */
    Maybe<V> filter(Condition<? super V> cond);

    /**
     * Retrieves a value from container.
     * @return - the value which this container has.
     * @throws NoSuchElementException - If this container has null value.
     */
    V get() throws NoSuchElementException;

    /**
     * Retrieves a value or returns default value.
     * @param defaultValue - default value.
     * @return - If this container has a value, returns the value. If not, default value will be returned.
     */
    V orDefault(V defaultValue);

    /**
     * Retrieves a value from container or throws an exception generated by the given generator.
     * @param gen - {@link Generator} instance.
     * @return - If this container has a value, returns the value. If not, an exception generated by given generator will be thrown.
     */
    V orThrow(Generator<? extends RuntimeException> gen);

    /**
     * Operating context interface allows {@link Nothing} instance to do some action.
     */
    interface OperatingContext {
        void orOnNothingDo(Task task);
    }

    /**
     * Let container do some action regardless of the presence of value.
     * @param op action for value.
     * @return Operating context.
     */
    OperatingContext onSomeDo(Operator<? super V> op);

    /**
     * Does an action when this container has value.
     * @param op
     */
    void whenSome(Operator<? super V> op);

    /**
     * Creates {@link Nothing} instance.
     * @param <T> type
     * @return {@link Nothing} instance.
     */
    @NotNull
    static <T> Maybe<T> nothing() {
        return new Nothing<>();
    }

    /**
     * Creates {@link Some} instance.
     * @param value a value. Null value will cause an exception.
     * @param <T> type of the value.
     * @return {@link Some} instance which contains the given value.
     * @throws EvaluatingException - thrown If given value is null
     */
    @Contract("null -> fail")
    @NotNull
    static <T> Maybe<T> some(T value) throws EvaluatingException {
        verifyNotNullObject(value);
        return new Some<>(value);
    }

    /**
     * Creates {@link Maybe} instance. If a given value is null, {@link Nothing} instance will be returned.
     * If a given value is not null, {@link Some} instance will be returned.
     * @param value - a value.
     * @param <T> type of the value.
     * @return {@link Maybe} instance.
     */
    @NotNull
    static <T> Maybe<T> maybe(T value) {
        if (value == null) {
            return new Nothing<>();
        } else {
            return new Some<>(value);
        }
    }
}
