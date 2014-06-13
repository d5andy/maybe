maybe
=====
``` java
package com.ubs.opsit.pcl.core.functions;

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


package com.ubs.opsit.pcl.core.functions;

import fj.F;
import fj.Function;
import fj.data.Either;
import fj.data.Option;

import static com.ubs.opsit.pcl.core.functions.Which.eitherStatic;

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

package com.ubs.opsit.pcl.core.functions;

import fj.F;
import fj.data.Either;
import fj.data.Option;

public final class Cond<X> implements F<X, Option<Exception>> {
	private static final Either ALWAYS_TRUE = Either.right(Boolean.TRUE);

	F<X, Either<Exception, Boolean>>[] conditions;

	private Cond(F<X, Either<Exception, Boolean>>[] conditions) {
		this.conditions = conditions;
	}

	public Option<Exception> f(X msg) {
		for (F<X, Either<Exception, Boolean>> f : conditions) {
			Either<Exception, Boolean> result = f.f(msg);
			if (result.isLeft()) {
				return Option.some(result.left().value());
			} else if (result.isRight() && result.right().value()) {
				return Option.none();
			}
		}
		return Option.none();
	}

	private static class Statement<X> implements F<X, Either<Exception, Boolean>> {
		final F<X, Either<Exception, Boolean>> match;
		final F<X, Option<Exception>> process;

		public Statement(F<X, Either<Exception, Boolean>> match, F<X, Option<Exception>> process) {
			this.match = match;
			this.process = process;
		}

		public Either<Exception, Boolean> f(X value) {
			Either<Exception, Boolean> matching = match.f(value);
			if (matching.isRight() && matching.right().value()) {
				Option<Exception> result = process.f(value);
				return (Either<Exception, Boolean>) (result.isSome() ? Either.left(result.some()) : matching);
			}
			return matching;
		}
	}

	public static <X> F<X, Either<Exception, Boolean>> statement(F<X, Either<Exception, Boolean>> match, F<X, Option<Exception>> process) {
		return new Statement(match, process);
	}

	public static <X> Cond<X> cond(F<X, Either<Exception, Boolean>>... conditions) {
		return new Cond<X>(conditions);
	}

	public static <X> F<X, Either<Exception, Boolean>> defaultStatement(F<X, Option<Exception>> defaultF) {
		F<X, Either<Exception, Boolean>> defaultM = new F<X, Either<Exception, Boolean>>() {
			public Either<Exception, Boolean> f(X x) {
				return ALWAYS_TRUE;
			}
		};
		return statement(defaultM, defaultF);
	}

}
```
