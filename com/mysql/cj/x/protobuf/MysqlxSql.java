package com.mysql.cj.x.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MysqlxSql {
  public static void registerAllExtensions(ExtensionRegistryLite registry) {}
  
  public static void registerAllExtensions(ExtensionRegistry registry) {
    registerAllExtensions((ExtensionRegistryLite)registry);
  }
  
  public static final class StmtExecute extends GeneratedMessageV3 implements StmtExecuteOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private int bitField0_;
    
    public static final int NAMESPACE_FIELD_NUMBER = 3;
    
    private volatile Object namespace_;
    
    public static final int STMT_FIELD_NUMBER = 1;
    
    private ByteString stmt_;
    
    public static final int ARGS_FIELD_NUMBER = 2;
    
    private List<MysqlxDatatypes.Any> args_;
    
    public static final int COMPACT_METADATA_FIELD_NUMBER = 4;
    
    private boolean compactMetadata_;
    
    private byte memoizedIsInitialized;
    
    private StmtExecute(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private StmtExecute() {
      this.memoizedIsInitialized = -1;
      this.namespace_ = "sql";
      this.stmt_ = ByteString.EMPTY;
      this.args_ = Collections.emptyList();
      this.compactMetadata_ = false;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private StmtExecute(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      int mutable_bitField0_ = 0;
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          ByteString bs;
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
            case 10:
              this.bitField0_ |= 0x2;
              this.stmt_ = input.readBytes();
              continue;
            case 18:
              if ((mutable_bitField0_ & 0x4) != 4) {
                this.args_ = new ArrayList<>();
                mutable_bitField0_ |= 0x4;
              } 
              this.args_.add(input.readMessage(MysqlxDatatypes.Any.PARSER, extensionRegistry));
              continue;
            case 26:
              bs = input.readBytes();
              this.bitField0_ |= 0x1;
              this.namespace_ = bs;
              continue;
            case 32:
              this.bitField0_ |= 0x4;
              this.compactMetadata_ = input.readBool();
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        if ((mutable_bitField0_ & 0x4) == 4)
          this.args_ = Collections.unmodifiableList(this.args_); 
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecute_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecute_fieldAccessorTable.ensureFieldAccessorsInitialized(StmtExecute.class, Builder.class);
    }
    
    public boolean hasNamespace() {
      return ((this.bitField0_ & 0x1) == 1);
    }
    
    public String getNamespace() {
      Object ref = this.namespace_;
      if (ref instanceof String)
        return (String)ref; 
      ByteString bs = (ByteString)ref;
      String s = bs.toStringUtf8();
      if (bs.isValidUtf8())
        this.namespace_ = s; 
      return s;
    }
    
    public ByteString getNamespaceBytes() {
      Object ref = this.namespace_;
      if (ref instanceof String) {
        ByteString b = ByteString.copyFromUtf8((String)ref);
        this.namespace_ = b;
        return b;
      } 
      return (ByteString)ref;
    }
    
    public boolean hasStmt() {
      return ((this.bitField0_ & 0x2) == 2);
    }
    
    public ByteString getStmt() {
      return this.stmt_;
    }
    
    public List<MysqlxDatatypes.Any> getArgsList() {
      return this.args_;
    }
    
    public List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList() {
      return (List)this.args_;
    }
    
    public int getArgsCount() {
      return this.args_.size();
    }
    
    public MysqlxDatatypes.Any getArgs(int index) {
      return this.args_.get(index);
    }
    
    public MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int index) {
      return this.args_.get(index);
    }
    
    public boolean hasCompactMetadata() {
      return ((this.bitField0_ & 0x4) == 4);
    }
    
    public boolean getCompactMetadata() {
      return this.compactMetadata_;
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      if (!hasStmt()) {
        this.memoizedIsInitialized = 0;
        return false;
      } 
      for (int i = 0; i < getArgsCount(); i++) {
        if (!getArgs(i).isInitialized()) {
          this.memoizedIsInitialized = 0;
          return false;
        } 
      } 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      if ((this.bitField0_ & 0x2) == 2)
        output.writeBytes(1, this.stmt_); 
      for (int i = 0; i < this.args_.size(); i++)
        output.writeMessage(2, (MessageLite)this.args_.get(i)); 
      if ((this.bitField0_ & 0x1) == 1)
        GeneratedMessageV3.writeString(output, 3, this.namespace_); 
      if ((this.bitField0_ & 0x4) == 4)
        output.writeBool(4, this.compactMetadata_); 
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      if ((this.bitField0_ & 0x2) == 2)
        size += 
          CodedOutputStream.computeBytesSize(1, this.stmt_); 
      for (int i = 0; i < this.args_.size(); i++)
        size += 
          CodedOutputStream.computeMessageSize(2, (MessageLite)this.args_.get(i)); 
      if ((this.bitField0_ & 0x1) == 1)
        size += GeneratedMessageV3.computeStringSize(3, this.namespace_); 
      if ((this.bitField0_ & 0x4) == 4)
        size += 
          CodedOutputStream.computeBoolSize(4, this.compactMetadata_); 
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof StmtExecute))
        return super.equals(obj); 
      StmtExecute other = (StmtExecute)obj;
      boolean result = true;
      result = (result && hasNamespace() == other.hasNamespace());
      if (hasNamespace())
        result = (result && getNamespace().equals(other.getNamespace())); 
      result = (result && hasStmt() == other.hasStmt());
      if (hasStmt())
        result = (result && getStmt().equals(other.getStmt())); 
      result = (result && getArgsList().equals(other.getArgsList()));
      result = (result && hasCompactMetadata() == other.hasCompactMetadata());
      if (hasCompactMetadata())
        result = (result && getCompactMetadata() == other.getCompactMetadata()); 
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      if (hasNamespace()) {
        hash = 37 * hash + 3;
        hash = 53 * hash + getNamespace().hashCode();
      } 
      if (hasStmt()) {
        hash = 37 * hash + 1;
        hash = 53 * hash + getStmt().hashCode();
      } 
      if (getArgsCount() > 0) {
        hash = 37 * hash + 2;
        hash = 53 * hash + getArgsList().hashCode();
      } 
      if (hasCompactMetadata()) {
        hash = 37 * hash + 4;
        hash = 53 * hash + Internal.hashBoolean(
            getCompactMetadata());
      } 
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static StmtExecute parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data);
    }
    
    public static StmtExecute parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecute parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data);
    }
    
    public static StmtExecute parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecute parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data);
    }
    
    public static StmtExecute parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecute)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecute parseFrom(InputStream input) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static StmtExecute parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static StmtExecute parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static StmtExecute parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static StmtExecute parseFrom(CodedInputStream input) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static StmtExecute parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecute)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(StmtExecute prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSql.StmtExecuteOrBuilder {
      private int bitField0_;
      
      private Object namespace_;
      
      private ByteString stmt_;
      
      private List<MysqlxDatatypes.Any> args_;
      
      private RepeatedFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> argsBuilder_;
      
      private boolean compactMetadata_;
      
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecute_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecute_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSql.StmtExecute.class, Builder.class);
      }
      
      private Builder() {
        this.namespace_ = "sql";
        this.stmt_ = ByteString.EMPTY;
        this
          .args_ = Collections.emptyList();
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        this.namespace_ = "sql";
        this.stmt_ = ByteString.EMPTY;
        this.args_ = Collections.emptyList();
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSql.StmtExecute.alwaysUseFieldBuilders)
          getArgsFieldBuilder(); 
      }
      
      public Builder clear() {
        super.clear();
        this.namespace_ = "sql";
        this.bitField0_ &= 0xFFFFFFFE;
        this.stmt_ = ByteString.EMPTY;
        this.bitField0_ &= 0xFFFFFFFD;
        if (this.argsBuilder_ == null) {
          this.args_ = Collections.emptyList();
          this.bitField0_ &= 0xFFFFFFFB;
        } else {
          this.argsBuilder_.clear();
        } 
        this.compactMetadata_ = false;
        this.bitField0_ &= 0xFFFFFFF7;
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecute_descriptor;
      }
      
      public MysqlxSql.StmtExecute getDefaultInstanceForType() {
        return MysqlxSql.StmtExecute.getDefaultInstance();
      }
      
      public MysqlxSql.StmtExecute build() {
        MysqlxSql.StmtExecute result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSql.StmtExecute buildPartial() {
        MysqlxSql.StmtExecute result = new MysqlxSql.StmtExecute(this);
        int from_bitField0_ = this.bitField0_;
        int to_bitField0_ = 0;
        if ((from_bitField0_ & 0x1) == 1)
          to_bitField0_ |= 0x1; 
        result.namespace_ = this.namespace_;
        if ((from_bitField0_ & 0x2) == 2)
          to_bitField0_ |= 0x2; 
        result.stmt_ = this.stmt_;
        if (this.argsBuilder_ == null) {
          if ((this.bitField0_ & 0x4) == 4) {
            this.args_ = Collections.unmodifiableList(this.args_);
            this.bitField0_ &= 0xFFFFFFFB;
          } 
          result.args_ = this.args_;
        } else {
          result.args_ = this.argsBuilder_.build();
        } 
        if ((from_bitField0_ & 0x8) == 8)
          to_bitField0_ |= 0x4; 
        result.compactMetadata_ = this.compactMetadata_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSql.StmtExecute)
          return mergeFrom((MysqlxSql.StmtExecute)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSql.StmtExecute other) {
        if (other == MysqlxSql.StmtExecute.getDefaultInstance())
          return this; 
        if (other.hasNamespace()) {
          this.bitField0_ |= 0x1;
          this.namespace_ = other.namespace_;
          onChanged();
        } 
        if (other.hasStmt())
          setStmt(other.getStmt()); 
        if (this.argsBuilder_ == null) {
          if (!other.args_.isEmpty()) {
            if (this.args_.isEmpty()) {
              this.args_ = other.args_;
              this.bitField0_ &= 0xFFFFFFFB;
            } else {
              ensureArgsIsMutable();
              this.args_.addAll(other.args_);
            } 
            onChanged();
          } 
        } else if (!other.args_.isEmpty()) {
          if (this.argsBuilder_.isEmpty()) {
            this.argsBuilder_.dispose();
            this.argsBuilder_ = null;
            this.args_ = other.args_;
            this.bitField0_ &= 0xFFFFFFFB;
            this.argsBuilder_ = MysqlxSql.StmtExecute.alwaysUseFieldBuilders ? getArgsFieldBuilder() : null;
          } else {
            this.argsBuilder_.addAllMessages(other.args_);
          } 
        } 
        if (other.hasCompactMetadata())
          setCompactMetadata(other.getCompactMetadata()); 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasStmt())
          return false; 
        for (int i = 0; i < getArgsCount(); i++) {
          if (!getArgs(i).isInitialized())
            return false; 
        } 
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSql.StmtExecute parsedMessage = null;
        try {
          parsedMessage = (MysqlxSql.StmtExecute)MysqlxSql.StmtExecute.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSql.StmtExecute)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public boolean hasNamespace() {
        return ((this.bitField0_ & 0x1) == 1);
      }
      
      public String getNamespace() {
        Object ref = this.namespace_;
        if (!(ref instanceof String)) {
          ByteString bs = (ByteString)ref;
          String s = bs.toStringUtf8();
          if (bs.isValidUtf8())
            this.namespace_ = s; 
          return s;
        } 
        return (String)ref;
      }
      
      public ByteString getNamespaceBytes() {
        Object ref = this.namespace_;
        if (ref instanceof String) {
          ByteString b = ByteString.copyFromUtf8((String)ref);
          this.namespace_ = b;
          return b;
        } 
        return (ByteString)ref;
      }
      
      public Builder setNamespace(String value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.namespace_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearNamespace() {
        this.bitField0_ &= 0xFFFFFFFE;
        this.namespace_ = MysqlxSql.StmtExecute.getDefaultInstance().getNamespace();
        onChanged();
        return this;
      }
      
      public Builder setNamespaceBytes(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x1;
        this.namespace_ = value;
        onChanged();
        return this;
      }
      
      public boolean hasStmt() {
        return ((this.bitField0_ & 0x2) == 2);
      }
      
      public ByteString getStmt() {
        return this.stmt_;
      }
      
      public Builder setStmt(ByteString value) {
        if (value == null)
          throw new NullPointerException(); 
        this.bitField0_ |= 0x2;
        this.stmt_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearStmt() {
        this.bitField0_ &= 0xFFFFFFFD;
        this.stmt_ = MysqlxSql.StmtExecute.getDefaultInstance().getStmt();
        onChanged();
        return this;
      }
      
      private void ensureArgsIsMutable() {
        if ((this.bitField0_ & 0x4) != 4) {
          this.args_ = new ArrayList<>(this.args_);
          this.bitField0_ |= 0x4;
        } 
      }
      
      public List<MysqlxDatatypes.Any> getArgsList() {
        if (this.argsBuilder_ == null)
          return Collections.unmodifiableList(this.args_); 
        return this.argsBuilder_.getMessageList();
      }
      
      public int getArgsCount() {
        if (this.argsBuilder_ == null)
          return this.args_.size(); 
        return this.argsBuilder_.getCount();
      }
      
      public MysqlxDatatypes.Any getArgs(int index) {
        if (this.argsBuilder_ == null)
          return this.args_.get(index); 
        return (MysqlxDatatypes.Any)this.argsBuilder_.getMessage(index);
      }
      
      public Builder setArgs(int index, MysqlxDatatypes.Any value) {
        if (this.argsBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureArgsIsMutable();
          this.args_.set(index, value);
          onChanged();
        } else {
          this.argsBuilder_.setMessage(index, (AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder setArgs(int index, MysqlxDatatypes.Any.Builder builderForValue) {
        if (this.argsBuilder_ == null) {
          ensureArgsIsMutable();
          this.args_.set(index, builderForValue.build());
          onChanged();
        } else {
          this.argsBuilder_.setMessage(index, (AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addArgs(MysqlxDatatypes.Any value) {
        if (this.argsBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureArgsIsMutable();
          this.args_.add(value);
          onChanged();
        } else {
          this.argsBuilder_.addMessage((AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder addArgs(int index, MysqlxDatatypes.Any value) {
        if (this.argsBuilder_ == null) {
          if (value == null)
            throw new NullPointerException(); 
          ensureArgsIsMutable();
          this.args_.add(index, value);
          onChanged();
        } else {
          this.argsBuilder_.addMessage(index, (AbstractMessage)value);
        } 
        return this;
      }
      
      public Builder addArgs(MysqlxDatatypes.Any.Builder builderForValue) {
        if (this.argsBuilder_ == null) {
          ensureArgsIsMutable();
          this.args_.add(builderForValue.build());
          onChanged();
        } else {
          this.argsBuilder_.addMessage((AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addArgs(int index, MysqlxDatatypes.Any.Builder builderForValue) {
        if (this.argsBuilder_ == null) {
          ensureArgsIsMutable();
          this.args_.add(index, builderForValue.build());
          onChanged();
        } else {
          this.argsBuilder_.addMessage(index, (AbstractMessage)builderForValue.build());
        } 
        return this;
      }
      
      public Builder addAllArgs(Iterable<? extends MysqlxDatatypes.Any> values) {
        if (this.argsBuilder_ == null) {
          ensureArgsIsMutable();
          AbstractMessageLite.Builder.addAll(values, this.args_);
          onChanged();
        } else {
          this.argsBuilder_.addAllMessages(values);
        } 
        return this;
      }
      
      public Builder clearArgs() {
        if (this.argsBuilder_ == null) {
          this.args_ = Collections.emptyList();
          this.bitField0_ &= 0xFFFFFFFB;
          onChanged();
        } else {
          this.argsBuilder_.clear();
        } 
        return this;
      }
      
      public Builder removeArgs(int index) {
        if (this.argsBuilder_ == null) {
          ensureArgsIsMutable();
          this.args_.remove(index);
          onChanged();
        } else {
          this.argsBuilder_.remove(index);
        } 
        return this;
      }
      
      public MysqlxDatatypes.Any.Builder getArgsBuilder(int index) {
        return (MysqlxDatatypes.Any.Builder)getArgsFieldBuilder().getBuilder(index);
      }
      
      public MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int index) {
        if (this.argsBuilder_ == null)
          return this.args_.get(index); 
        return (MysqlxDatatypes.AnyOrBuilder)this.argsBuilder_.getMessageOrBuilder(index);
      }
      
      public List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList() {
        if (this.argsBuilder_ != null)
          return this.argsBuilder_.getMessageOrBuilderList(); 
        return Collections.unmodifiableList((List)this.args_);
      }
      
      public MysqlxDatatypes.Any.Builder addArgsBuilder() {
        return (MysqlxDatatypes.Any.Builder)getArgsFieldBuilder().addBuilder(
            (AbstractMessage)MysqlxDatatypes.Any.getDefaultInstance());
      }
      
      public MysqlxDatatypes.Any.Builder addArgsBuilder(int index) {
        return (MysqlxDatatypes.Any.Builder)getArgsFieldBuilder().addBuilder(index, 
            (AbstractMessage)MysqlxDatatypes.Any.getDefaultInstance());
      }
      
      public List<MysqlxDatatypes.Any.Builder> getArgsBuilderList() {
        return getArgsFieldBuilder().getBuilderList();
      }
      
      private RepeatedFieldBuilderV3<MysqlxDatatypes.Any, MysqlxDatatypes.Any.Builder, MysqlxDatatypes.AnyOrBuilder> getArgsFieldBuilder() {
        if (this.argsBuilder_ == null) {
          this
            
            .argsBuilder_ = new RepeatedFieldBuilderV3(this.args_, ((this.bitField0_ & 0x4) == 4), (AbstractMessage.BuilderParent)getParentForChildren(), isClean());
          this.args_ = null;
        } 
        return this.argsBuilder_;
      }
      
      public boolean hasCompactMetadata() {
        return ((this.bitField0_ & 0x8) == 8);
      }
      
      public boolean getCompactMetadata() {
        return this.compactMetadata_;
      }
      
      public Builder setCompactMetadata(boolean value) {
        this.bitField0_ |= 0x8;
        this.compactMetadata_ = value;
        onChanged();
        return this;
      }
      
      public Builder clearCompactMetadata() {
        this.bitField0_ &= 0xFFFFFFF7;
        this.compactMetadata_ = false;
        onChanged();
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final StmtExecute DEFAULT_INSTANCE = new StmtExecute();
    
    public static StmtExecute getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<StmtExecute> PARSER = (Parser<StmtExecute>)new AbstractParser<StmtExecute>() {
        public MysqlxSql.StmtExecute parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSql.StmtExecute(input, extensionRegistry);
        }
      };
    
    public static Parser<StmtExecute> parser() {
      return PARSER;
    }
    
    public Parser<StmtExecute> getParserForType() {
      return PARSER;
    }
    
    public StmtExecute getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static final class StmtExecuteOk extends GeneratedMessageV3 implements StmtExecuteOkOrBuilder {
    private static final long serialVersionUID = 0L;
    
    private byte memoizedIsInitialized;
    
    private StmtExecuteOk(GeneratedMessageV3.Builder<?> builder) {
      super(builder);
      this.memoizedIsInitialized = -1;
    }
    
    private StmtExecuteOk() {
      this.memoizedIsInitialized = -1;
    }
    
    public final UnknownFieldSet getUnknownFields() {
      return this.unknownFields;
    }
    
    private StmtExecuteOk(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null)
        throw new NullPointerException(); 
      UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              continue;
          } 
          if (!parseUnknownField(input, unknownFields, extensionRegistry, tag))
            done = true; 
        } 
      } catch (InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (IOException e) {
        throw (new InvalidProtocolBufferException(e)).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      } 
    }
    
    public static final Descriptors.Descriptor getDescriptor() {
      return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecuteOk_descriptor;
    }
    
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
      return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecuteOk_fieldAccessorTable.ensureFieldAccessorsInitialized(StmtExecuteOk.class, Builder.class);
    }
    
    public final boolean isInitialized() {
      byte isInitialized = this.memoizedIsInitialized;
      if (isInitialized == 1)
        return true; 
      if (isInitialized == 0)
        return false; 
      this.memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(CodedOutputStream output) throws IOException {
      this.unknownFields.writeTo(output);
    }
    
    public int getSerializedSize() {
      int size = this.memoizedSize;
      if (size != -1)
        return size; 
      size = 0;
      size += this.unknownFields.getSerializedSize();
      this.memoizedSize = size;
      return size;
    }
    
    public boolean equals(Object obj) {
      if (obj == this)
        return true; 
      if (!(obj instanceof StmtExecuteOk))
        return super.equals(obj); 
      StmtExecuteOk other = (StmtExecuteOk)obj;
      boolean result = true;
      result = (result && this.unknownFields.equals(other.unknownFields));
      return result;
    }
    
    public int hashCode() {
      if (this.memoizedHashCode != 0)
        return this.memoizedHashCode; 
      int hash = 41;
      hash = 19 * hash + getDescriptor().hashCode();
      hash = 29 * hash + this.unknownFields.hashCode();
      this.memoizedHashCode = hash;
      return hash;
    }
    
    public static StmtExecuteOk parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data);
    }
    
    public static StmtExecuteOk parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecuteOk parseFrom(ByteString data) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data);
    }
    
    public static StmtExecuteOk parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecuteOk parseFrom(byte[] data) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data);
    }
    
    public static StmtExecuteOk parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
      return (StmtExecuteOk)PARSER.parseFrom(data, extensionRegistry);
    }
    
    public static StmtExecuteOk parseFrom(InputStream input) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static StmtExecuteOk parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static StmtExecuteOk parseDelimitedFrom(InputStream input) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    
    public static StmtExecuteOk parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    
    public static StmtExecuteOk parseFrom(CodedInputStream input) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    
    public static StmtExecuteOk parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
      return 
        (StmtExecuteOk)GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    
    public Builder newBuilderForType() {
      return newBuilder();
    }
    
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    
    public static Builder newBuilder(StmtExecuteOk prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    
    public Builder toBuilder() {
      return (this == DEFAULT_INSTANCE) ? new Builder() : (new Builder())
        .mergeFrom(this);
    }
    
    protected Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MysqlxSql.StmtExecuteOkOrBuilder {
      public static final Descriptors.Descriptor getDescriptor() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecuteOk_descriptor;
      }
      
      protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecuteOk_fieldAccessorTable
          .ensureFieldAccessorsInitialized(MysqlxSql.StmtExecuteOk.class, Builder.class);
      }
      
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      
      private void maybeForceBuilderInitialization() {
        if (MysqlxSql.StmtExecuteOk.alwaysUseFieldBuilders);
      }
      
      public Builder clear() {
        super.clear();
        return this;
      }
      
      public Descriptors.Descriptor getDescriptorForType() {
        return MysqlxSql.internal_static_Mysqlx_Sql_StmtExecuteOk_descriptor;
      }
      
      public MysqlxSql.StmtExecuteOk getDefaultInstanceForType() {
        return MysqlxSql.StmtExecuteOk.getDefaultInstance();
      }
      
      public MysqlxSql.StmtExecuteOk build() {
        MysqlxSql.StmtExecuteOk result = buildPartial();
        if (!result.isInitialized())
          throw newUninitializedMessageException(result); 
        return result;
      }
      
      public MysqlxSql.StmtExecuteOk buildPartial() {
        MysqlxSql.StmtExecuteOk result = new MysqlxSql.StmtExecuteOk(this);
        onBuilt();
        return result;
      }
      
      public Builder clone() {
        return (Builder)super.clone();
      }
      
      public Builder setField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.setField(field, value);
      }
      
      public Builder clearField(Descriptors.FieldDescriptor field) {
        return (Builder)super.clearField(field);
      }
      
      public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
        return (Builder)super.clearOneof(oneof);
      }
      
      public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
        return (Builder)super.setRepeatedField(field, index, value);
      }
      
      public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
        return (Builder)super.addRepeatedField(field, value);
      }
      
      public Builder mergeFrom(Message other) {
        if (other instanceof MysqlxSql.StmtExecuteOk)
          return mergeFrom((MysqlxSql.StmtExecuteOk)other); 
        super.mergeFrom(other);
        return this;
      }
      
      public Builder mergeFrom(MysqlxSql.StmtExecuteOk other) {
        if (other == MysqlxSql.StmtExecuteOk.getDefaultInstance())
          return this; 
        mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        MysqlxSql.StmtExecuteOk parsedMessage = null;
        try {
          parsedMessage = (MysqlxSql.StmtExecuteOk)MysqlxSql.StmtExecuteOk.PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (InvalidProtocolBufferException e) {
          parsedMessage = (MysqlxSql.StmtExecuteOk)e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null)
            mergeFrom(parsedMessage); 
        } 
        return this;
      }
      
      public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.setUnknownFields(unknownFields);
      }
      
      public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
        return (Builder)super.mergeUnknownFields(unknownFields);
      }
    }
    
    private static final StmtExecuteOk DEFAULT_INSTANCE = new StmtExecuteOk();
    
    public static StmtExecuteOk getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }
    
    @Deprecated
    public static final Parser<StmtExecuteOk> PARSER = (Parser<StmtExecuteOk>)new AbstractParser<StmtExecuteOk>() {
        public MysqlxSql.StmtExecuteOk parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
          return new MysqlxSql.StmtExecuteOk(input, extensionRegistry);
        }
      };
    
    public static Parser<StmtExecuteOk> parser() {
      return PARSER;
    }
    
    public Parser<StmtExecuteOk> getParserForType() {
      return PARSER;
    }
    
    public StmtExecuteOk getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }
  }
  
  public static Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }
  
  static {
    String[] descriptorData = { "\n\020mysqlx_sql.proto\022\nMysqlx.Sql\032\fmysqlx.proto\032\026mysqlx_datatypes.proto\"\n\013StmtExecute\022\026\n\tnamespace\030\003 \001(\t:\003sql\022\f\n\004stmt\030\001 \002(\f\022#\n\004args\030\002 \003(\0132\025.Mysqlx.Datatypes.Any\022\037\n\020compact_metadata\030\004 \001(\b:\005false:\004ê0\f\"\025\n\rStmtExecuteOk:\004ê0\021B\031\n\027com.mysql.cj.x.protobuf" };
    Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
          MysqlxSql.descriptor = root;
          return null;
        }
      };
    Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[] { Mysqlx.getDescriptor(), 
          MysqlxDatatypes.getDescriptor() }, assigner);
  }
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Sql_StmtExecute_descriptor = getDescriptor().getMessageTypes().get(0);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Sql_StmtExecute_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Sql_StmtExecute_descriptor, new String[] { "Namespace", "Stmt", "Args", "CompactMetadata" });
  
  private static final Descriptors.Descriptor internal_static_Mysqlx_Sql_StmtExecuteOk_descriptor = getDescriptor().getMessageTypes().get(1);
  
  private static final GeneratedMessageV3.FieldAccessorTable internal_static_Mysqlx_Sql_StmtExecuteOk_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_Mysqlx_Sql_StmtExecuteOk_descriptor, new String[0]);
  
  private static Descriptors.FileDescriptor descriptor;
  
  static {
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    registry.add(Mysqlx.clientMessageId);
    registry.add(Mysqlx.serverMessageId);
    Descriptors.FileDescriptor.internalUpdateFileDescriptor(descriptor, registry);
    Mysqlx.getDescriptor();
    MysqlxDatatypes.getDescriptor();
  }
  
  public static interface StmtExecuteOkOrBuilder extends MessageOrBuilder {}
  
  public static interface StmtExecuteOrBuilder extends MessageOrBuilder {
    boolean hasNamespace();
    
    String getNamespace();
    
    ByteString getNamespaceBytes();
    
    boolean hasStmt();
    
    ByteString getStmt();
    
    List<MysqlxDatatypes.Any> getArgsList();
    
    MysqlxDatatypes.Any getArgs(int param1Int);
    
    int getArgsCount();
    
    List<? extends MysqlxDatatypes.AnyOrBuilder> getArgsOrBuilderList();
    
    MysqlxDatatypes.AnyOrBuilder getArgsOrBuilder(int param1Int);
    
    boolean hasCompactMetadata();
    
    boolean getCompactMetadata();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\x\protobuf\MysqlxSql.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */