package fj.maybe;

import fj.F;
import fj.Function;
import fj.data.Either;

public final class Which<A, B, C> implements F<A, Either<B, C>> {
	private F<A, Either<B, C>> f;

	private Which(F<A, Either<B, C>> f) {
		this.f = f;
	}

	public Either<B, C> f(A result) {
		return f.f(result);
	}

	public <X> F<A, X> either(final F<B, X> left, final F<C, X> right) {
		return Function.andThen(this, eitherStatic(left, right));
	}

	public static <A, B, C> Which<A, B, C> which(F<A, Either<B, C>> func) {
		return new Which<>(func);
	}

	public static <A, B, C> F<Either<A, B>, C> eitherStatic(final F<A, C> left, final F<B, C> right) {
		return new F<Either<A, B>, C>() {
			public C f(Either<A, B> input) {
				return input.either(left, right);
			}
		};
	}

}
