@echo off

@rem 구글 포매터는 https://github.com/google/google-java-format/releases 에서 다운 받으실 수 있습니다.
for /r %%f in (*.java) do (
    google-java-format_windows-x86-64.exe --replace "%%f"
)
