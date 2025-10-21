package com.tmq.sql;

public class WriterRepositorySql {
    public static final String FIND_ALL_SQL = """
            SELECT * FROM writers
            """;

    public static final String FIND_BY_ID_SQL = """
            SELECT * FROM writers
            WHERE id = ?;
            """;

    public static final String FIND_BY_NAME_SQL = """
            SELECT * FROM writers
            WHERE first_name like ? and last_name like ?;
    """;

    public static final String SAVE_SQL = """
            INSERT INTO writers(first_name, last_name)
            VALUES (?, ?);
            """;

    public static final String DELETE_BY_ID_SQL = """
            DELETE FROM writers
            WHERE id = ?;
    """;

    public static final String UPDATE_SQL = """
            UPDATE writers
            SET first_name = ?, last_name = ?
            WHERE id = ?;
            """;
}
