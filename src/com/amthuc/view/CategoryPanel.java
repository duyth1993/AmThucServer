/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amthuc.view;

import com.amthuc.dao.CategoryDAO;
import com.amthuc.model.Category;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Pia
 */
public class CategoryPanel extends javax.swing.JPanel {

    public static int c_id = -1;
    public static String c_name;

    /**
     * Creates new form CategoryPanel
     */
    public CategoryPanel() {
        initComponents();
        initTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategory = new javax.swing.JTable();
        txtName = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        btnOpenDish = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAdd.setText("Thêm mới");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("DANH MỤC MÓN ĂN");

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tblCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "Tên", "Mô tả"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblCategory);

        txtDesc.setColumns(20);
        txtDesc.setRows(5);
        jScrollPane2.setViewportView(txtDesc);

        btnOpenDish.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnOpenDish.setText("Mở danh sách món");
        btnOpenDish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenDishActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Mã");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Tên");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Mô tả");

        txtId.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(125, 125, 125)
                        .addComponent(jLabel4)
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnOpenDish))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(216, 216, 216)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(333, 333, 333)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOpenDish, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String err = checkForm();
        if (err.equals("")) {
            try {
                CategoryDAO categoryDAO = new CategoryDAO();
                Category cate = new Category();
                cate.setName(txtName.getText().toString().trim());
                cate.setDescription(txtDesc.getText().toString().trim());
                cate.setImage("day la image");
                int insert = categoryDAO.insert(cate);
                if (insert == 1) {
                    txtName.setText("");
                    txtId.setText("");
                    txtDesc.setText("");
                    initTable();
                    showMessage("Thêm mới thành công!");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
            } catch (SQLException ex) {
                Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
            }
        } else {
            showMessage(err);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnOpenDishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenDishActionPerformed
        if (c_id != -1) {
            new DishFrame().setVisible(true);
        }
    }//GEN-LAST:event_btnOpenDishActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String err = checkForm();
        if (!txtId.getText().trim().equals("")) {
            if (err.equals("")) {
                try {
                    CategoryDAO categoryDAO = new CategoryDAO();
                    Category cate = new Category();
                    cate.setId(Integer.parseInt(txtId.getText().toString().trim()));
                    cate.setName(txtName.getText().toString().trim());
                    cate.setDescription(txtDesc.getText().toString().trim());
                    cate.setImage("day la image");
                    int update = categoryDAO.update(cate);
                    if (update == 1) {
                        txtName.setText("");
                        txtId.setText("");
                        txtDesc.setText("");
                        initTable();
                        showMessage("Cập nhật thành công!");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                    showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
                } catch (SQLException ex) {
                    Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                    showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
                }
            } else {
                showMessage(err);
            }
        } else {
            showMessage("Chưa chọn danh mục nào");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (!txtId.getText().trim().equals("")) {
            try {
                CategoryDAO categoryDAO = new CategoryDAO();
                int delete = categoryDAO.delete(Integer.parseInt(txtId.getText().toString().trim()));
                if (delete == 1) {
                    txtName.setText("");
                    txtId.setText("");
                    txtDesc.setText("");
                    initTable();
                    showMessage("Xóa thành công!");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
            } catch (SQLException ex) {
                Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
            }
        } else {
            showMessage("Chưa chọn danh mục nào");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private String checkForm() {
        String error = "";
        if (txtName.getText().toString().trim().equals("")) {
            error = "Tên danh mục không được để trống";
        }
        return error;
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOpenDish;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblCategory;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables

    private void initTable() {
        try {

            CategoryDAO dao = new CategoryDAO();
            ArrayList<Category> listCate = new ArrayList<>();
            listCate = (ArrayList<Category>) dao.getAll();
            System.out.println("" + listCate.size());
            Vector tblRecords = new Vector();
            Vector tblTitle = new Vector();
            tblTitle.add("Mã");
            tblTitle.add("Tên danh mục");
            tblTitle.add("Mô tả");
            for (Category lc : listCate) {
                Vector record = new Vector();
                record.add(lc.getId());
                record.add(lc.getName());
                record.add(lc.getDescription());
                tblRecords.add(record);
            }

            tblCategory.setModel(new DefaultTableModel(tblRecords, tblTitle));
            tblCategory.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int row = tblCategory.getSelectedRow();
                    txtId.setText(tblCategory.getValueAt(row, 0).toString());
                    txtName.setText(tblCategory.getValueAt(row, 1).toString());
                    txtDesc.setText(tblCategory.getValueAt(row, 2).toString());
                    c_id = Integer.parseInt(tblCategory.getValueAt(row, 0).toString());
                    c_name = tblCategory.getValueAt(row, 1).toString();
                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
            showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
        } catch (SQLException ex) {
            Logger.getLogger(CategoryPanel.class.getName()).log(Level.SEVERE, null, ex);
            showMessage("Có lỗi xảy ra ! Vui lòng thử lại sau.");
        }
    }
}
