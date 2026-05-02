package com.example.srp;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Integer;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class WatchlistDao_Impl implements WatchlistDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<WatchlistEntity> __insertAdapterOfWatchlistEntity;

  private final EntityDeleteOrUpdateAdapter<WatchlistEntity> __deleteAdapterOfWatchlistEntity;

  public WatchlistDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfWatchlistEntity = new EntityInsertAdapter<WatchlistEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `watchlist` (`symbol`,`name`,`changePercent`,`accuracy`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final WatchlistEntity entity) {
        if (entity.getSymbol() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getSymbol());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getName());
        }
        statement.bindDouble(3, entity.getChangePercent());
        statement.bindLong(4, entity.getAccuracy());
      }
    };
    this.__deleteAdapterOfWatchlistEntity = new EntityDeleteOrUpdateAdapter<WatchlistEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `watchlist` WHERE `symbol` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final WatchlistEntity entity) {
        if (entity.getSymbol() == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.getSymbol());
        }
      }
    };
  }

  @Override
  public Object addToWatchlist(final WatchlistEntity stock,
      final Continuation<? super Unit> $completion) {
    if (stock == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfWatchlistEntity.insert(_connection, stock);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object removeFromWatchlist(final WatchlistEntity stock,
      final Continuation<? super Unit> $completion) {
    if (stock == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __deleteAdapterOfWatchlistEntity.handle(_connection, stock);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Flow<List<WatchlistEntity>> getAllWatchlist() {
    final String _sql = "SELECT * FROM watchlist";
    return FlowUtil.createFlow(__db, false, new String[] {"watchlist"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfSymbol = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "symbol");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfChangePercent = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "changePercent");
        final int _columnIndexOfAccuracy = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "accuracy");
        final List<WatchlistEntity> _result = new ArrayList<WatchlistEntity>();
        while (_stmt.step()) {
          final WatchlistEntity _item;
          final String _tmpSymbol;
          if (_stmt.isNull(_columnIndexOfSymbol)) {
            _tmpSymbol = null;
          } else {
            _tmpSymbol = _stmt.getText(_columnIndexOfSymbol);
          }
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final double _tmpChangePercent;
          _tmpChangePercent = _stmt.getDouble(_columnIndexOfChangePercent);
          final int _tmpAccuracy;
          _tmpAccuracy = (int) (_stmt.getLong(_columnIndexOfAccuracy));
          _item = new WatchlistEntity(_tmpSymbol,_tmpName,_tmpChangePercent,_tmpAccuracy);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object isInWatchlist(final String symbol,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT EXISTS(SELECT * FROM watchlist WHERE symbol = ?)";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (symbol == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, symbol);
        }
        final Boolean _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp == null ? null : _tmp != 0;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
