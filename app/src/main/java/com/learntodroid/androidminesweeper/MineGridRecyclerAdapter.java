package com.learntodroid.androidminesweeper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MineGridRecyclerAdapter extends RecyclerView.Adapter<MineGridRecyclerAdapter.MineTileViewHolder> {
    private List<Cell> cells;
    private OnCellClickListener listener;

    public MineGridRecyclerAdapter(List<Cell> cells, OnCellClickListener listener) {
        this.cells = cells;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MineTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cell, parent, false);
        return new MineTileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MineTileViewHolder holder, int position) {
        holder.bind(cells.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return cells.size();
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
        notifyDataSetChanged();
    }

    class MineTileViewHolder extends RecyclerView.ViewHolder {
        TextView valueTextView;

        public MineTileViewHolder(@NonNull View itemView) {
            super(itemView);

            valueTextView = itemView.findViewById(R.id.item_cell_value);
        }

        public void bind(final Cell cell) {
            itemView.setBackgroundColor(Color.GRAY);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.cellClick(cell);
                }
            });

            if (cell.isRevealed()) {
                if (cell.getValue() == Cell.BOMB) {
                    valueTextView.setText(R.string.bomb);
                } else if (cell.getValue() == Cell.BLANK) {
                    valueTextView.setText("");
                    itemView.setBackgroundColor(Color.WHITE);
                } else {
                    valueTextView.setText(String.valueOf(cell.getValue()));
                    if (cell.getValue() == 1) {
                        valueTextView.setTextColor(Color.BLUE);
                    } else if (cell.getValue() == 2) {
                        valueTextView.setTextColor(Color.GREEN);
                    } else if (cell.getValue() == 3) {
                        valueTextView.setTextColor(Color.RED);
                    }
                }
            } else if (cell.isFlagged()) {
                valueTextView.setText(R.string.flag);
            }
        }
    }
}
