package ru.otus.java_2018_08.student.hw07.bank.acquire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw07.common.Money;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public enum AtmState {
    READY {
        @Override
        void insertCard(Atm atm, BooleanSupplier cardChecker) {
            switchIfTrue(AUTHORIZED, atm, cardChecker);
        }
    },
    AUTHORIZED {
        @Override
        void getCardBack(Atm atm, BooleanSupplier cardChecker) {
            switchIfTrue(READY, atm, cardChecker);
        }

        @Override
        boolean tryPutBanknote(Atm atm, BooleanSupplier trying) {
            return switchIfTrue(PUTTING, atm, trying);
        }

        @Override
        boolean tryPullBanknote(Atm atm, BooleanSupplier trying) {
            return switchIfTrue(PULLING, atm, trying);
        }

        @Override
        Money getBalance(Supplier<Money> getting) {
            return getting.get();
        }
    },
    PUTTING {
        @Override
        boolean putBanknote(Atm atm, BooleanSupplier putting) {
            return switchAnyway(AUTHORIZED, atm, putting);
        }
    },
    PULLING {
        @Override
        boolean pullBanknote(Atm atm, BooleanSupplier pulling) {
            return switchAnyway(AUTHORIZED, atm, pulling);
        }
    };

    static private Logger log = LoggerFactory.getLogger(AtmState.class);

    boolean switchIfTrue(AtmState state, Atm atm, BooleanSupplier action) {
        boolean result = action.getAsBoolean();

        if (result) {
            atm.state = state;

            log.info(state.name());
        }
        else {
            log.warn(this.name());
        }

        return result;
    }

    boolean switchAnyway(AtmState state, Atm atm, BooleanSupplier action) {
        boolean result = action.getAsBoolean();
        atm.state = state;

        log.info(state.name());

        return result;
    }

    void insertCard(Atm atm, BooleanSupplier cardChecker) {
        wrongAction();
    };

    void getCardBack(Atm atm, BooleanSupplier cardChecker) {
        wrongAction();
    }

    boolean tryPutBanknote(Atm atm, BooleanSupplier trying) {
        wrongAction();

        return false;
    };

    boolean tryPullBanknote(Atm atm, BooleanSupplier trying) {
        wrongAction();

        return false;
    }

    boolean putBanknote(Atm atm, BooleanSupplier putting) {
        wrongAction();

        return false;
    };

    boolean pullBanknote(Atm atm, BooleanSupplier pulling) {
        wrongAction();

        return false;
    }

    Money getBalance(Supplier<Money> getting) {
        wrongAction();

        return null;
    }

    private void wrongAction() {
        log.error(String.format(
                "Cannot change state %s", this.name()
        ));

        throw new RuntimeException("Wrong switch");
    }
}
