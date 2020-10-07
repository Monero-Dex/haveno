/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.desktop.main.funds.transactions;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import bisq.core.btc.wallet.XmrWalletService;
import monero.wallet.model.MoneroTxWallet;

public class DisplayedTransactionsTest {
    @Test
    public void testUpdate() {
        List<MoneroTxWallet> transactions = Lists.newArrayList(mock(MoneroTxWallet.class), mock(MoneroTxWallet.class));

        XmrWalletService walletService = mock(XmrWalletService.class);
        when(walletService.getTransactions(false)).thenReturn(transactions);

        TransactionListItemFactory transactionListItemFactory = mock(TransactionListItemFactory.class,
                RETURNS_DEEP_STUBS);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        DisplayedTransactions testedEntity = new DisplayedTransactions(
                walletService,
                mock(TradableRepository.class),
                transactionListItemFactory,
                mock(TransactionAwareTradableFactory.class));

        testedEntity.update();

        assertEquals(transactions.size(), testedEntity.size());
    }

    @Test
    public void testUpdateWhenRepositoryIsEmpty() {
        XmrWalletService walletService = mock(XmrWalletService.class);
        when(walletService.getTransactions(false))
                .thenReturn(Collections.singletonList(mock(MoneroTxWallet.class)));

        TradableRepository tradableRepository = mock(TradableRepository.class);
        when(tradableRepository.getAll()).thenReturn(Collections.emptySet());

        TransactionListItemFactory transactionListItemFactory = mock(TransactionListItemFactory.class);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        DisplayedTransactions testedEntity = new DisplayedTransactions(
                walletService,
                tradableRepository,
                transactionListItemFactory,
                mock(TransactionAwareTradableFactory.class));

        testedEntity.update();

        assertEquals(1, testedEntity.size());
        verify(transactionListItemFactory).create(any(), nullable(TransactionAwareTradable.class));
    }
}
