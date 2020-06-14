package com.example.wbudy_apka.math

class MatrixDouble(__rows: Int = 1, __columns: Int = 1, initValue: Double = 0.0) {
    private var _data: Array<Array<Double>> = Array<Array<Double>>(__rows,fun(row: Int): Array<Double>{
        return Array<Double>(__columns,fun(column: Int): Double{ return initValue })
    });
    var data: Array<Array<Double>>
        get() { return _data; }
        set(value) {
            if(value.size != rows) {
                throw IllegalArgumentException("Cannot set data of matrix")
            }
            for (row in value) {
                if(row.size != columns) {
                    throw IllegalArgumentException("Cannot set data of matrix")
                }
            }
            _data = value;
        }
    private var _rows: Int = __rows;
    private var _columns: Int = __columns;
    var rows: Int
        get() {return _rows;}
        private set(value) {_rows = value;}
    var columns: Int
        get() {return _columns;}
        private set(value) {_columns = value;}
    fun reset(data: Array<Array<Double>>) {
        val lrows = data.size;
        if(lrows < 1) {
            throw  IllegalArgumentException("Cannot set data of matrix")
        }
        val lcolumns = data[0].size;
        for(row in data) {
            if(lcolumns != row.size) {
                throw  IllegalArgumentException("Cannot set data of matrix")
            }
        }
        _data = data;
        _rows = lrows;
        _columns = lcolumns;
    }
    fun get(row: Int, column: Int): Double {
        return data[row][column];
    }
    fun set(row: Int, column: Int, value: Double) {
        data[row][column] = value;
    }

    override fun toString(): String {
        var x = "[\n"
        for( row in data) {
            x += "\t${row.contentToString()}\n"
        }
        x += "]\n"
        return x
    }

    fun transpose(): MatrixDouble {
        val c = MatrixDouble(columns,rows)
        for(row_index in 0..(rows-1)) {
            for(column_index in 0..(columns-1)) {
                c.set(column_index,row_index,get(row_index,column_index))
            }
        }
        return c
    }

    operator fun plus(b: MatrixDouble): MatrixDouble {
        if(this.rows != b.rows || this.columns != b.columns) {
            throw ArithmeticException("Cannot add two matrix with diffrent sizes");
        }
        val c = MatrixDouble(rows,columns);
        for(row_index in 0..(rows-1)) {
            for(column_index in 0..(columns-1)) {
                c.set(row_index,column_index,get(row_index,column_index)+b.get(row_index,column_index))
            }
        }
        return c
    }
    operator fun times(b: MatrixDouble): MatrixDouble {
        if(this.columns != b.rows) {
            throw ArithmeticException("Cannot multiply two matrix");
        }
        val c = MatrixDouble(this.rows,b.columns)
        for(row_cindex in 0..(c.rows-1)) {
            for (column_cindex in 0..(c.columns-1)) {
                var sum: Double = 0.0
                for(column_index in 0..(c.columns-1)) {
                    val a = this.get(row_cindex,column_index)
                    val b = b.get(column_index,column_cindex)
                    sum += a*b
                }
                c.set(row_cindex,column_cindex,sum)
            }
        }
        return c;
    }
}