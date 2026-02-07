import os

# 定义需要清理的两个路径
folders = [
    r'C:\python\code\ultralytics-8.4.12\datasets\pic\labels\train',
    r'C:\python\code\ultralytics-8.4.12\datasets\pic\labels\val'
]

for folder in folders:
    if not os.path.exists(folder):
        print(f"跳过不存在的文件夹: {folder}")
        continue

    print(f"正在清理文件夹: {folder}")
    files = os.listdir(folder)

    deleted_count = 0
    for file in files:
        # 检查是否以 .xml 结尾（忽略大小写）
        if file.lower().endswith('.xml'):
            file_path = os.path.join(folder, file)
            try:
                os.remove(file_path)
                deleted_count += 1
            except Exception as e:
                print(f"无法删除 {file}: {e}")

    print(f"成功删除 {deleted_count} 个 XML 文件。")

print("\n--- 清理完成！ ---")