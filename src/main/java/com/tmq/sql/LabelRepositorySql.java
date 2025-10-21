package com.tmq.sql;

public class LabelRepositorySql {
    public final static String FIND_ALL_SQL = """
            SELECT * FROM labels
            """;

    public final static String FIND_BY_ID_SQL = """
            SELECT * FROM labels WHERE id = ?
            """;

    public static final String FIND_BY_POST_ID = """
            SELECT * FROM posts_labels WHERE post_id = ?
            """;

    public static final String SAVE_LABEL_TO_POST = """
            INSERT INTO posts_labels (post_id, label_id) VALUES (?, ?)
            """;

    public final static String SAVE_SQL = """
            INSERT INTO labels (name)
            VALUES (?)
            ON CONFLICT (name) DO UPDATE SET name = excluded.name RETURNING id
            """;

    public final static String DELETE_SQL = """
            DELETE FROM labels WHERE id = ?
            """;

    public final static String UPDATE_SQL = """
            UPDATE labels SET name = ? WHERE id = ?
            """;

    public final static String FIND_BY_NAME = """
            SELECT * FROM labels WHERE name = ?
            """;
}
