package fj.maybe;

import fj.F;
import fj.Function;
import fj.data.Either;
import fj.data.Option;

import static fj.maybe.Which.eitherStatic;

public final class Maybe<A, B> implements F<A, Either<B, A>> {
	private F<A, Option<B>> f;

	private Maybe(F<A, Option<B>> f) {
		this.f = f;
	}

	public Either<B, A> f(A input) {
		Option<B> option = f.f(input);
		return option.isSome() ? Either.<B, A>left(option.some()) : Either.<B, A>right(input);
	}

	public <X> F<A, X> either(F<B, X> left, F<A, X> right) {
		return Function.andThen(this, eitherStatic(left, right));
	}

	public static <A, B> Maybe<A, B> maybe(F<A, Option<B>> optionF) {
		return new Maybe<>(optionF);
	}

}
