package fj.maybe;

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